import os
import mmap

# input file goes here
file = 'input.txt'
#output will take the original name file and add _reversed to the end of it and will keep the original extension.
reversed_file = os.path.splitext(file)[0]+'_reversed' + os.path.splitext(file)[-1]

# reading and writing file as binaries
with open(file, 'rb') as input, open(reversed_file, 'wb+') as output:
    # memory mapping allows optimal solution for large files' reversion. alternative way would've been to use os.SEEK + buffer size specification.
    memory_map = mmap.mmap(input.fileno(), 0, access=mmap.ACCESS_READ)
    output.write(memory_map[::-1])
    output.flush()

# tested on txt, csv, xlsx, mp3, mp4
# works fine with txt, csv, rest give corrupted file error when trying to open. re-reversing them back to original works too and they are not corrupted again.
# could've made it .exe file to be more runnable but for that you wouldn't be able to see the code inside plus would've needed to make at least cmd-like user interface 
# which is not very much runnable file