import csv

averages = []

with open('/Users/jacksonbremen/Downloads/20191113211328.sqliteonline.com.csv') as file:
	csv_reader = csv.reader(file, delimiter=',')
	line_count = 0
	for row in csv_reader:

print(averages)