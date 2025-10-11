task launch {
    doLast {
        def taskNames = gradle.startParameter.taskNames
        def launchIndex = taskNames.indexOf('launch')
        def passedArgs = []

        if (launchIndex >= 0 && launchIndex + 1 < taskNames.size()) {
            passedArgs = taskNames[(launchIndex + 1)..-1]
        }

        println "Args passed to launch task: $passedArgs"

        def isServer = passedArgs.contains('--s') || passedArgs.contains('--server')

        if (isServer) {
            println "Launching server..."
            if (System.properties['os.name'].toLowerCase().contains('windows')) { # why does this shit not work man
                exec {
                    commandLine 'cmd', '/c', 'gradlew.bat', 'runServer'
                }
            } else {
                exec {
                    commandLine './gradlew', 'runServer'
                }
        
         else {
            println "Launching client..."
            if (System.properties['os.name'].toLowerCase().contains('windows')) {
                exec {
                    commandLine 'cmd', '/c', 'gradlew.bat', 'runClient'
                }
            } else {
                exec {
                    commandLine './gradlew', 'runClient'
                }
