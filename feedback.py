#!/usr/bin/env python

import sys, getopt, json, codecs
import mistune
import pystache
from os import path

template = """Hey there {{speaker}},

Thank you so much for helping make Chicago Roboto a big success.  As promised, here are your session feedback results.

Session: **{{session}}**

Average Score:

{{average_table}}

Dump of scores:

{{table}}

We hope to see you in 2018!

Best

Jerrell, John and Ryan"""

def print_usage():
	print "feedback.py -i <inputfile> -o <outputfile>"

def main(argv):
	inputfile=''
	outputfile=''
	try:
		opts, args = getopt.getopt(argv, "hi:o:",["ifile=","ofile="])
	except getopt.GetoptError:
		print_usage()
		sys.exit(2)

	for opt, arg in opts:
		if opt == '-h':
			print_usage()
			sys.exit()
		elif opt in ("-i", "--ifile"):
			inputfile = arg
		elif opt in ("-o", "--ofile"):
			outputfile = arg

	with open(inputfile) as f:
		data = json.load(f)
		data = data['events']['chicagoroboto-2017']

		speakers = data['speakers']
		sessions = data['sessions']
		feedback = data['feedback']

		for session_id in feedback.iterkeys():
			content = {}
			session = sessions[session_id]
			content['session'] = session['name']

			# Feedback Table
			feedback_table_template = "| Overall | Speaker | Technical |\n"
			feedback_table_template += "| -------:| -------:| ---------:|\n"

			session_feedback = feedback[session_id]
			overall_avg = float(sum(f['overall'] for k, f in session_feedback.iteritems())) / len(session_feedback)
			speaker_avg = float(sum(f['speaker'] for k, f in session_feedback.iteritems())) / len(session_feedback)
			tech_avg = float(sum(f['technical'] for k, f in session_feedback.iteritems())) / len(session_feedback)
			content['average_table'] = feedback_table_template
			content['average_table'] += "| {:.2f} | {:.2f} | {:.2f} |".format(overall_avg, speaker_avg, tech_avg)

			content['table'] = feedback_table_template
			for key, fb in session_feedback.iteritems():
				content['table'] += "| {} | {} | {} |\n".format(fb['overall'], fb['speaker'], fb['technical'])

			session_speakers = []
			if 'speakers' in session:
				session_speakers = [speakers[speaker_id] for speaker_id in session['speakers']]

			for speaker in session_speakers:
				content['speaker'] = speaker['name']
				
				email = mistune.markdown(pystache.render(template, content))

				filename = "{}-{}.html".format(speaker['id'], session_id)
				with codecs.open(path.join(outputfile, filename), "w", "utf-8") as f:
					f.write(email)


if __name__ == "__main__":
	main(sys.argv[1:])
