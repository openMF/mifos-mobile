//
//  ContentView.swift
//  iosApp
//
//  Created by Apple on 14/08/24.
//

import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        Text(Greeting().greet())
        .padding()
    }
}

#Preview {
    ContentView()
}
