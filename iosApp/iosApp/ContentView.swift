struct ComposeView: UIViewControllerRepresentable {
    let aiTaskRepository: AiTaskRepository
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(aiTaskRepository: aiTaskRepository)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    let aiTaskRepository: AiTaskRepository
    
    var body: some View {
        ComposeView(aiTaskRepository: aiTaskRepository)
                .ignoresSafeArea(.all, edges: .bottom) // Compose has own keyboard handler
    }
}
