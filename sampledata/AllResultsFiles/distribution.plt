reset
#n=100 #number of intervals
stats filename u 1 nooutput
max=STATS_max #max value
min=STATS_min #min value
width=(max-min)/n #interval width
#function used to map a value to the intervals
hist(x,width)=width*floor(x/width)+width/2.0
set term png #output terminal and file
set output "histogram.png"
set xrange [min:max]
#to put an empty boundary around the
#data inside an autoscaled graph.
set offset graph 0.05,0.05,0.05,0.0
set xtics min,(max-min)/5,max
set boxwidth width*0.9
set style fill solid 0.5 #fillstyle
set tics out nomirror
set xlabel "x"
set ylabel "Frequency"
#count and plot

set title filename

plot filename u (hist($1,width)):(1.0) smooth freq w boxes lc rgb"green" notitle
