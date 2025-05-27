import Foundation
import SwiftUI
import GoogleMobileAds
import UIKit
import SwiftUI
import ComposeApp

class IOSPlatformAdViewControllerFactory : PlatformAdViewControllerFactory {
    func getSizeOfAdType(adType: AdType) -> AdSizePx {
        let adSize = getAdSizeFromType(adType: adType)
        let scale = UIScreen.main.scale
        let widthPx =  adSize.size.width * scale
        let heightPx =  adSize.size.height * scale
        return AdSizePx(width: Float(widthPx), height: Float(heightPx))
    }
    
    func generateBannerAdViewController(adType: AdType) -> UIViewController {
        let adBannerView = BannerViewContainer(getAdSizeFromType(adType: adType))
       
        return UIHostingController(rootView: adBannerView)
    }

    private func getAdSizeFromType(adType: AdType) -> AdSize {
        switch adType {
            case .banner:
                return AdSizeBanner
            case .largeBanner:
                return AdSizeLargeBanner
            case .mediumRectangle:
                return AdSizeMediumRectangle
            case .fullBanner:
                return AdSizeFullBanner
            case .leaderboard:
                return AdSizeLeaderboard
            case .wideSkyscraper:
                return AdSizeSkyscraper
            default:
                fatalError("Unknown ad type: \(adType)")
        }
    }
}

// [START create_banner_view]
private struct BannerViewContainer: UIViewRepresentable {
  let adSize: AdSize

  init(_ adSize: AdSize) {
    self.adSize = adSize
  }

  func makeUIView(context: Context) -> UIView {
    // Wrap the GADBannerView in a UIView. GADBannerView automatically reloads a new ad when its
    // frame size changes; wrapping in a UIView container insulates the GADBannerView from size
    // changes that impact the view returned from makeUIView.
    let view = UIView()
    view.addSubview(context.coordinator.bannerView)
    return view
  }

  func updateUIView(_ uiView: UIView, context: Context) {
    context.coordinator.bannerView.adSize = adSize
  }

  func makeCoordinator() -> BannerCoordinator {
    return BannerCoordinator(self)
  }
  // [END create_banner_view]

  // [START create_banner]
  class BannerCoordinator: NSObject, BannerViewDelegate {

    private(set) lazy var bannerView: BannerView = {
      let banner = BannerView(adSize: parent.adSize)
      // [START load_ad]
      #if DEBUG
        banner.adUnitID = "ca-app-pub-3940256099942544/2435281174"
      #else
        banner.adUnitID = "ca-app-pub-9577779442408289/1414068685"
      #endif
      banner.load(Request())
      // [END load_ad]
      // [START set_delegate]
      banner.delegate = self
      // [END set_delegate]
      return banner
    }()

    let parent: BannerViewContainer

    init(_ parent: BannerViewContainer) {
      self.parent = parent
    }
    // [END create_banner]

    // MARK: - GADBannerViewDelegate methods

    func bannerViewDidReceiveAd(_ bannerView: BannerView) {
      print("DID RECEIVE AD.")
    }

    func bannerView(_ bannerView: BannerView, didFailToReceiveAdWithError error: Error) {
      print("FAILED TO RECEIVE AD: \(error.localizedDescription)")
    }
  }
}
