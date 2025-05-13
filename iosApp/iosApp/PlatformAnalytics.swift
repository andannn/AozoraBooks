//
//  PlatformAnalytics.swift
//  iosApp
//
//  Created by JHC on 2025/05/13.
//  Copyright © 2025 orgName. All rights reserved.
//

import ComposeApp

class IOSAnalytics : PlatformAnalytics {
    func logEvent(event: String, params: [String : String]) {
        print("invoke log event in swift")
    }
}
