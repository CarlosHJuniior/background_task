#import "BackgroundtaskPlugin.h"
#if __has_include(<backgroundtask/backgroundtask-Swift.h>)
#import <backgroundtask/backgroundtask-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "backgroundtask-Swift.h"
#endif

@implementation BackgroundtaskPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBackgroundtaskPlugin registerWithRegistrar:registrar];
}
@end
