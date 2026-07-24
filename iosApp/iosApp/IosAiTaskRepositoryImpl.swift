import Foundation
import Shared
import FirebaseVertexAI

class IosAiTaskRepositoryImpl: AiTaskRepository {
    
    private lazy var model = VertexAI.vertexAI().generativeModel(modelName: "gemini-3.1-flash-lite")
    
    func parseReminderText(userInput: String) async throws -> ParsedTask {
        let now = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/yyyy"
        let today = dateFormatter.string(from: now)
        
        dateFormatter.dateFormat = "hh:mm a"
        let currentTime = dateFormatter.string(from: now)
        
        let prompt = """
            Today's date is \(today) and current time is \(currentTime).
            You are a helpful assistant that extracts task details from natural language.
            The input may be in English, Hindi, or Romanized Hindi (like 'kal subah').

            Extract the following details and respond ONLY with a valid JSON object, no markdown, no code fences, no explanation.

            Rules:
            - title: short, clear action in English (max 6 words), strip filler like "remind me to"
            - description: extra context in English, empty string "" if none
            - date: resolve relative dates ("kal", "tomorrow", "next Monday") into "DD/MM/YYYY" relative to \(today). Use null only if no date/time reference exists at all.
            - time: resolve into 12-hour format "hh:mm AM/PM" (e.g., 09:00 AM, 11:30 PM). 
              * Use the current time (\(currentTime)) to infer AM/PM if not specified. 
              * If the specified time has already passed today, assume the user means the next occurrence (e.g., if it's 10:00 PM and user says '11:30', they mean '11:30 PM').
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
        
        do {
            let response = try await model.generateContent(prompt)
            guard let responseText = response.text else {
                throw NSError(domain: "AiTaskRepository", code: 1, userInfo: [NSLocalizedDescriptionKey: "Empty response from AI"])
            }
            
            Logger.shared.debug(tag: "IosAiTaskRepository", message: { "AI Response: \(responseText)" })
            
            // Extract JSON using Regex
            let pattern = "\\{[\\s\\S]*\\}"
            let regex = try NSRegularExpression(pattern: pattern)
            let nsString = responseText as NSString
            let results = regex.matches(in: responseText, range: NSRange(location: 0, length: nsString.length))
            
            guard let firstMatch = results.first else {
                throw NSError(domain: "AiTaskRepository", code: 2, userInfo: [NSLocalizedDescriptionKey: "Could not find JSON in response"])
            }
            
            let jsonString = nsString.substring(with: firstMatch.range)
            guard let jsonData = jsonString.data(using: .utf8) else {
                throw NSError(domain: "AiTaskRepository", code: 3, userInfo: [NSLocalizedDescriptionKey: "Invalid JSON encoding"])
            }
            
            let jsonObject = try JSONSerialization.jsonObject(with: jsonData) as? [String: Any]
            
            return ParsedTask(
                title: jsonObject?["title"] as? String ?? "",
                description: jsonObject?["description"] as? String ?? "",
                date: jsonObject?["date"] as? String,
                time: jsonObject?["time"] as? String,
                importance: jsonObject?["importance"] as? String ?? "Medium",
                category: jsonObject?["category"] as? String ?? "Personal"
            )
        } catch {
            Logger.shared.error(throwable: nil, tag: "IosAiTaskRepository", message: { "AI Parsing Error: \(error.localizedDescription)" })
            throw error
        }
    }
}
