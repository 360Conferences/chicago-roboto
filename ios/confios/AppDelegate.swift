//
//  AppDelegate.swift
//  confios
//
//  Created by Ryan Harter on 2019-04-25.
//  Copyright © 2019 Chicago Roboto. All rights reserved.
//

import UIKit
import Firebase
import Swinject

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    static var shared: AppDelegate!

    private var assembler: Assembler!
    var resolver: Resolver! {
        get { return self.assembler.resolver }
    }
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        FirebaseApp.configure()
        AppDelegate.shared = self
        assembler = Assembler([
            DataAssembly(eventId: "chicagoroboto-2019"),
            AppAssembly()
        ])

        self.window = UIWindow(frame: UIScreen.main.bounds)

        let sessionListFactory = AppDelegate.shared.resolver.resolve(SessionListViewControllerFactory.self)!
        let sessionListViewController = sessionListFactory.create(date: "2019-04-25")
        let rootViewController = UINavigationController(rootViewController: sessionListViewController)
        self.window?.makeKeyAndVisible()
        self.window?.rootViewController = rootViewController

        return true
    }


    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.

    }


    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.

    }


    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }


    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }


    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


}
