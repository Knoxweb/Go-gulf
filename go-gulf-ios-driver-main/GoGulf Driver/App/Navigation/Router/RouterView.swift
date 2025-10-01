//
//  RouterView.swift
//  NavigationCheck
//
//  Created by Prabin Phasikawo on 04/02/2024.
//

import SwiftUI

struct RouterView<Content: View>: View {
    @StateObject var router: Router = Router()
    // Our root view content
    @EnvironmentObject var appRootManager: AppRootManager

    private let content: Content

    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content()
    }

    var body: some View {
        NavigationStack(path: $router.path) {
            content
                .navigationDestination(for: Router.Route.self) { route in
                    router.view(for: route)
                }
        }
        .environmentObject(router)
        
    }
}
