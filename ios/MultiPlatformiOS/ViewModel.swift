//
//  ViewModel.swift
//  MultiPlatformiOS
//
//  Created by Samuel Daniel on 10/07/2020.
//  Copyright © 2020 Gabriel Coman. All rights reserved.
//

import Foundation
import SharedCode

@available(iOS 13.0, *)
class TodoViewModel: ObservableObject {
    @Published var todos = [Todo_]()
    @Published var isError = false
    
    private let repository: TodoRepository
    
    
    init(repository : TodoRepository) {
        self.repository = repository
    }
    
      /**
          Pull todo todo tasks from remote
       */
    func fetch(fromRemote : Bool){
        repository.fetchTodoData(onSuccess: { [weak self] data in
            self?.todos = data
        }, onFailure: {[weak self] throwable in
                self?.isError
        }, fromRemote: fromRemote)
    }
    
    
    /**
        insert task into local repository and emit changes
     */
    func insertTodoItem(title : String) {
        let todo = Todo_(id: 0, title: title, completed: false)
        repository.cacheTodoData(todo: todo)
        
        fetch(fromRemote: false)
    }
    
 
    
}
