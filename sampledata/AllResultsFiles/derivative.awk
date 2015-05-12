#! /usr/bin/awk -f
#BEGIN { firstline = 1 }
{
	if( NR == 1 ) {
		old = $1
	}else {
		print ($1-old)
		old = $1
	}
}

