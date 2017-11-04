Build Status master branch [![Build Status master branch](https://travis-ci.org/jwermuth/gradle-task-helpers.svg?branch=master)](https://travis-ci.org/jwermuth/gradle-task-helpers)

** This repository is not fully operational yet. If you use the watch feature I will notify once its operational **

# Whats this for
It helps us at Danske Spil and Lund&Bendsen to test our gradle plugins.

Does this sound familiar :

I want to test the plugin i am writing. In particular, I want to mock it, so I do not _actually_ perform the actions when 
 testing. Its not easy, because the test is run in a separate VM, so anyhing I set in a 
static var, a factory or other code based state is not present when the test is run.

If so, this jar can help you. It gives you a base class that you can extend to build a mock-able gradle 'service', 
e.g. a service that commits changes to you code (which you do not want to do when testing the plugin)

