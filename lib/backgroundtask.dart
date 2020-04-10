import 'dart:async';

import 'package:flutter/services.dart';

class Backgroundtask {
  static const MethodChannel _channel =
      const MethodChannel('backgroundtask');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
