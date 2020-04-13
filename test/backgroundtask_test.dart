import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:backgroundtask/backgroundtask.dart';

void main() {
  const MethodChannel channel = MethodChannel('backgroundtask');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getAndroidId', () async {
    expect(await Backgroundtask.androidVersion, '42');
  });
}
