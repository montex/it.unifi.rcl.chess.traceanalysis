# CHESS Statistical Trace Analyzer
### it.unifi.rcl.chess.traceanalysis

This software is part of the **CHESS Framework**, and it has been developed within the **CONCERTO Project**.  
More information about CHESS and CONCERTO can be found at the following websites:  

* http://chess-project.org/  
* http://concerto-project.eu/
  
The tool has been developed by the [Resilient Computing Lab](http://rcl.dsi.unifi.it) team, of the University of Firenze, Italy, and it is released under the GPL license. Please see the LICENSE and COPYRIGHT files for more information.

# Purpose

This software is used to analyze the statistical properties of run-time execution traces of different variables. Its application domain is **real-time safety-critical systems**. Key variables like *release jitter*, *execution time*, etc. are analyzed to devise probabilistic upper bounds, which are guaranteed to be respected with a given given coverage.

# Usage

The application is a Java GUI. Simply run the **main** method in  
**it.unifi.rcl.chess.traceanalysis.gui.TraceAnalyzerGUI**

Parameters:

* **-examples**  
    Work with example traces. An 'example' menu will be available to load the examples. 

# Libraries
 
 This program makes use of third party libraries, each having its own
 license and copyright holder:
 
   * **ADAPTARE**	(*lib/adaptare.jar*)  
     Copyright (c) 2010 LASIGE, University of Lisboa.  
     http://www.lasige.di.fc.ul.pt/node/2836   
     Licensed under the Apache License, Version 2.0;
     you may not use this library except in compliance with the License.
     You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0  
     
   * **JCommon**	(*lib/jcommon.jar*)  
     (C) Copyright 2000-2016, by Object Refinery Limited and Contributors.  
     http://www.jfree.org/jcommon/  
     This library is free software; you can redistribute it and/or
     modify it under the terms of the GNU Lesser General Public
     License as published by the Free Software Foundation; either
     version 2.1 of the License, or (at your option) any later version.  
   
   * **JFreeChart** (*lib/jfreechart.jar*)  
     (C) Copyright 2000-2016, by Object Refinery Limited and Contributors.  
     http://www.jfree.org/jfreechart/  
     This library is free software; you can redistribute it and/or
     modify it under the terms of the GNU Lesser General Public
     License as published by the Free Software Foundation; either
     version 2.1 of the License, or (at your option) any later version.  
