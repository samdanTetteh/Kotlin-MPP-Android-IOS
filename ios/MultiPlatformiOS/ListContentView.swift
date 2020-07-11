//
//  ListContentView.swift
//  MultiPlatformiOS
//
//  Created by Samuel Daniel on 11/07/2020.
//  Copyright © 2020 Gabriel Coman. All rights reserved.
//


import SwiftUI
import SharedCode

@available(iOS 13.0, *)
struct ListContentView: View {
    
    @ObservedObject var todoViewModel = TodoViewModel(repository: TodoRepository())
    
    
    var body: some View {
        NavigationView {
         VStack {
            List(todoViewModel.todos, id: \.id){ todo in
              ListViewItem(todo: todo)
            }
         }
        .navigationBarTitle("Multiplatform", displayMode: .large)
         .navigationBarItems(trailing: Button(action: {
            withAnimation {
                self.promptForAnswer()
            }
         }) {
            Text("Add Task")
            } )
       }
      .onAppear(perform: {
         self.todoViewModel.fetch(fromRemote: true)
    }).alert(isPresented: $todoViewModel.isError, content: {
        Alert(
            title: Text("Error"),
            message: Text("Error : Cannot load Todo items"),
            dismissButton: .default(Text("OK")){}
        )
    })
}
    
    
    
    /***
         View to hold task items in VStack
     */
    struct ListViewItem: View {
        private let todo : Todo_
        
        init(todo : Todo_) {
            self.todo = todo
        }
        
        var body: some View{
            HStack{
                Image(systemName: todo.completed ? "checkmark.circle.fill" : "circle")
                     .resizable()
                     .frame(width: 20, height: 20)
                Text(todo.title)
            }
        }
    }
    

    
    /**
     display text dialog view to add new task*
    */
    func promptForAnswer() {
        let ac = UIAlertController(title: "Add New Task", message: nil, preferredStyle: .alert)
        ac.addTextField()

        let submitAction = UIAlertAction(title: "Submit", style: .default) { [unowned ac] _ in
            let answer = ac.textFields![0]
            if(!answer.text!.isEmpty){
                self.todoViewModel.insertTodoItem(title: answer.text!)
            }

            
        }

        ac.addAction(submitAction)

        let rootViewController = UIApplication.shared.keyWindow?.rootViewController
        rootViewController?.present(ac, animated: true)
    }
    
    


}









