#/usr/bin/python

import os, shutil, sys, getopt, json
from pprint import pprint
from jinja2 import Environment, FileSystemLoader

class Speaker:
    name = ''
    email = ''

    def __init__(self, name, email):
        self.name = name
        self.email = email

    def __repr__(self):
        return "Speaker[name={}]".format(self.name)

class Feedback:
    def __init__(self):
        self.session = ''
        self.speakers = []
        self.speaker = []
        self.technical = []
        self.overall = []

    def __repr__(self):
        return "Feedback[session={}, speakers={}, speaker={}, technical={}, overall={}]".format(self.session, self.speakers, self.speaker, self.technical, self.overall)

def parse_feedback(feedback, sessions, speakers):
    fs = []
    for session_id in feedback:
        fb = Feedback()

        session = sessions[session_id]
        if 'speakers' not in session:
            continue

        fb.session = session['name']

        for speaker_id in session['speakers']:
            speaker = speakers[speaker_id]
            fb.speakers.append(Speaker(speaker['name'], ''))#speaker['email']))

        for fb_key in feedback[session_id]:
            fb.speaker.append(feedback[session_id][fb_key]['speaker'])
            fb.technical.append(feedback[session_id][fb_key]['technical'])
            fb.overall.append(feedback[session_id][fb_key]['overall'])
                

        fs.append(fb)
    return fs

def usage():
    print("{}: Generates emails based on attendee feedback.".format(sys.argv[0]))
    print("")
    print("-i, --infile      The exported json data from your Firebase instance")
    print("-t, --template    The email template file to use [default=\"./template.html\"")
    print("-o, --outdir      The output directory to write the emails to [default=\"./output\"")

def main(argv):
    inputfile = ''
    outputdir = './output'
    template = './template.html'
    try:
        opts, args = getopt.getopt(argv, "i:t:o:", ["infile=", "template=", "outdir="])
    except getopt.GetoptError:
        usage()
        sys.exit(2)

    if not inputfile:
        usage()
        sys.exit(2)

    for opt, arg in opts:
        if opt in ("-i", "--infile"):
            inputfile = arg
        elif opt in ("-t", "--template"):
            template = arg
        elif opt in ("-o", "--outdir"):
            outputdir = arg

    print("cleaning output dir: {}".format(outputdir))
    if os.path.isfile(outputdir) or os.path.isdir(outputdir):
        shutil.rmtree(outputdir)
    os.mkdir(outputdir)

    j2_env = Environment(loader=FileSystemLoader(os.getcwd()),
                         trim_blocks=True)
    template = j2_env.get_template(template)

    with open(inputfile) as file:
        data = json.load(file)

        for session_id in data['feedback']:

            session = data['sessions'][session_id]
            if 'speakers' not in session:
                continue

            for speaker_id in session['speakers']:
                fbs = data['feedback'][session_id].values()
                averages = {
                    'speaker': 0, 
                    'technical': 0,
                    'overall': 0
                }
                for fb in fbs:
                    averages['speaker'] += fb['speaker']
                    averages['technical'] += fb['technical']
                    averages['overall'] += fb['overall']

                averages['speaker'] /= len(fbs)
                averages['technical'] /= len(fbs)
                averages['overall'] /= len(fbs)

                email = template.render(speaker=data['speakers'][speaker_id], session=session, averages=averages, feedback=data['feedback'][session_id].values())
                with open(os.path.join(outputdir, "{}-{}.html".format(speaker_id, session_id)), "wb") as f:
                    f.write(email)
                print("wrote {}-{} email".format(speaker_id, session_id))

if __name__ == "__main__":
    main(sys.argv[1:])


