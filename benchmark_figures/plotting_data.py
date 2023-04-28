import matplotlib.pyplot as plt
import pandas as pd
import argparse
import os
import sys

parser = argparse.ArgumentParser()

parser.add_argument('-i1', '--input1', dest='input1', help='input file1', type=str)
parser.add_argument('-i2', '--input2', dest='input2', help='input file2', type=str)
parser.add_argument('-o', '--output', dest='output', help='output file', type=str)

options = parser.parse_args()

if options.input1 is None:
    parser.print_usage()
    sys.exit()

if options.input2 is None:
    parser.print_usage()
    sys.exit()


data1 = pd.read_csv(options.input1, delimiter=',')
data2 = pd.read_csv(options.input2, delimiter=',')

print(data1.columns[0])

# Uses the first column for the x axes
ax = data.plot(x=data.columns[0], marker='o', xticks=data.iloc[:,0])

# Set the bottom value to 0 for the Y axes
ax.set_ylim(bottom=0)

ax.set_xlabel('Problem size', fontsize='x-large')
ax.set_ylabel('Speedup', fontsize='x-large')

# setting font sizes
ax.legend(fontsize='x-large')
plt.yticks(fontsize='x-large')
plt.xticks(fontsize='x-large')

# To have a graph that can be easily included in another document
plt.tight_layout(pad=0.5)


# filename for the output
if options.output is None:
    prefix, ext = os.path.splitext(options.input)
    outname = prefix + '.pdf'
else:
    outname = options.output

#plt.savefig(outname, format='pdf', dpi=1200)
plt.savefig(outname, format='png', dpi=1200)

plt.show()



