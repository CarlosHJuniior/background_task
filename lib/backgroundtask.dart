import 'dart:async';

import 'package:flutter/services.dart';

class Backgroundtask {
  static const MethodChannel _channel = const MethodChannel('lince.com/backgroundtask');

  static Future<void> androidVersion() async {
    await _channel.invokeMethod('periodic');
  }
}
