import UIKit
import SwiftUI




class ViewController: UIViewController {
    
    @IBOutlet var containerView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        /**Embed swiftUI view in ViewController
        Checks for plateform version ie. 13 and above
         */
        if #available(iOS 13.0, *) {
            let childView = UIHostingController(rootView: ListContentView())
            addChild(childView)
            childView.view.frame = containerView.bounds
            containerView.addSubview(childView.view)
            childView.didMove(toParent: self)
        }
        
        
    
    }
}

