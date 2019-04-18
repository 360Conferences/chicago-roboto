# Feedback

Feedback submitted in the app will be entered in the web into a `feedback` collection.

```
"feedback": {
  "[talk-id]": {
    "[user-id]": {
      "overall": [0-5],
      "technical": [0-4],
      "speaker": [0-5]
    },
    ...
  },
  ...
}
```

# Email Templating

The `generate_emails.py` script in this directory allows you to easily process the feedback data to generate HTML for sending emails to speakers.

To generate emails, you should update the template in `template.html`, export all of your data from the Firebase console, then run the script, pointing it at the exported json file.

```sh
python generate_emails.py -i ~/Downloads/devcon-export.json
```

Resulting html files in `output` can be sent to speakers. (To easily send the formatted emails via Gmail/Inbox, simply open the html file in a browser, and copy and paste it into your email).