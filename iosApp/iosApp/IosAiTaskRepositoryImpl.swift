import Foundation
import FirebaseAI
import Shared

class IosAiTaskRepositoryImpl: NSObject, AiTaskRepository {
    private let model = FirebaseAI.firebaseAI(backend: .googleAI()).generativeModel(modelName: "gemini-3.1-flash-lite")

    func parseReminderText(userInput: String, completionHandler: @escaping (ParsedTask?, Error?) -> Void) {
        Swift.Task {
            do {
                let result = try await self.parseInternal(userInput: userInput)
                completionHandler(result, nil)
            } catch {
                completionHandler(nil, error)
            }
        }
    }

    private func parseInternal(userInput: String) async throws -> ParsedTask {
        let df = DateFormatter()
        df.dateFormat = "dd/MM/yyyy"
        let todayStr = df.string(from: Date())
        
        let tf = DateFormatter()
        tf.dateFormat = "hh:mm a"
        let currentTimeStr = tf.string(from: Date())

        let prompt = """
            Today's date is \(todayStr) and current time is \(currentTimeStr).
            You are a helpful assistant that extracts task details from natural language.
            The input may be in English, Hindi, or Romanized Hindi (like 'kal subah').

            Extract the following details and respond ONLY with a valid JSON object, no markdown, no code fences, no explanation.

            Rules:
            - title: short, clear action in English (max 6 words), strip filler like "remind me to"
            - description: extra context in English, empty string "" if none
            - date: resolve relative dates ("kal", "tomorrow", "next Monday") into "DD/MM/YYYY" relative to \(todayStr). Use null only if no date/time reference exists at all.
            - time: resolve into 12-hour format "hh:mm AM/PM" (e.g., 09:00 AM, 11:30 PM). 
              * Use the current time (\(currentTimeStr)) to infer AM/PM if not specified. 
              * If the specified time has already passed today, assume the user means the next occurrence.
              * If a date is mentioned but no time, use null. If time is mentioned but no date, assume today.
            - importance: infer from urgency words. "urgent"/"asap"/"important"/"zaroori" → "High". "whenever"/"low priority"/"fursat mein" → "Low". No urgency signal → "Medium".
            - category: choose exactly one: "Work", "Personal", or "Shopping"
              - "Work": meetings, deadlines, emails, colleagues, projects, client calls
              - "Shopping": buy, purchase, order, pick up, grocery, store, kharidna
              - "Personal": everything else (family, health, errands, self-care)

            JSON structure:
            {
              "title": "...",
              "description": "...",
              "date": "DD/MM/YYYY or null (Example: 17/07/2026)",
              "time": "hh:mm AM/PM or null (Example: 08:30 AM, 11:30 PM)",
              "importance": "High|Medium|Low",
              "category": "Work|Personal|Shopping"
            }

            Input: "\(userInput)"
        """

        let response = try await model.generateContent(prompt)
        guard let text = response.text else {
            throw NSError(domain: "IosAiTaskRepository", code: 0, userInfo: [NSLocalizedDescriptionKey: "Empty response from AI"])
        }

        let pattern = "\\{[\\s\\S]*\\}"
        guard let regex = try? NSRegularExpression(pattern: pattern, options: []),
              let match = regex.firstMatch(in: text, range: NSRange(text.startIndex..., in: text)) else {
            throw NSError(domain: "IosAiTaskRepository", code: 1, userInfo: [NSLocalizedDescriptionKey: "Could not find JSON in response"])
        }

        let jsonStr = String(text[Range(match.range, in: text)!])
        guard let data = jsonStr.data(using: .utf8),
              let json = try JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            throw NSError(domain: "IosAiTaskRepository", code: 2, userInfo: [NSLocalizedDescriptionKey: "Failed to parse JSON"])
        }

        return ParsedTask(
            title: json["title"] as? String ?? "",
            description: json["description"] as? String ?? "",
            date: json["date"] as? String,
            time: json["time"] as? String,
            importance: json["importance"] as? String ?? "Medium",
            category: json["category"] as? String ?? "Personal"
        )
    }
}
