import 'package:android_alarm_manager/android_alarm_manager.dart';
import 'package:backgroundtask/backgroundtask.dart';
import 'package:flutter/material.dart';
import 'package:mailer/mailer.dart';
import 'package:mailer/smtp_server.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'example',
      debugShowCheckedModeBanner: false,
      home: MyPage(),
    );
  }
}

class MyPage extends StatefulWidget {
  @override
  _MyPageState createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  @override
  void initState() {
    super.initState();
    Backgroundtask.periodic(callback: taskEmail);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Center(child: CircularProgressIndicator()),
    );
  }
}

void taskEmail() async {
  final String username = 'flutter.background@gmail.com';
  final String password = '@123456789!';

  final SmtpServer smtpServer = gmail(username, password);

  final Message message = Message()
    ..from = Address(username, 'Background Test')
    ..recipients.add('carlos.hjunior@lince.com.br')
    ..subject = 'Test Dart Mailer library :: 😀 :: ${DateTime.now()}'
    ..text = 'This is the plain text.\nThis is line 2 of the text part.';
  
  try {
    await send(message, smtpServer);
  } catch (e, _) {
    print('erro >> $e');
  }
}
