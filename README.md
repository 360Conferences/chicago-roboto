# Chicago Roboto
This is the app for Chicago Roboto 2017. It is based entirely on the [Firebase](https://firebase.google.com) data in `data.json`.

The app should be easily customizable to other developer conferences by simply:

1. Setting up your own Firebase instance, and replacing the `google-services.json` file with your own.
1. Updating the `data.json` file with your conference details, keeping the same structure, and uploading it to Firebase.
1. Updating graphical assets and strings.

**Note:** There is one place in the app that is currently hard coded, and that is the speaker avatar URL.  You can simply update this in `Speaker.kt:12` until it goes away.

Once a session has started, users will have the option to submit feedback for the session. Check the `feedback` directory for scripts to process that feedback and generate emails to send to speakers.

# Architecture

The app uses an MVP architecture in which the interfaces, presenters and models are contained in a Java only `core` module, and the Android components, like Firebase integration (`providers`), custom View implementations, etc. are in the Android `app` module.

You will also find no Java in the project, as it is entirely written in [Kotlin](https://kotlinlang.org/).

# TODO

1. Tests
1. Reduce the repetition in the data.json sessions.
1. Replace the hard coded image path.
1. Add support for sponsors.

# License

Copyright 2016 Ryan Harter.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
