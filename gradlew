#!/usr/bin/envsh

#
#Copyright2015theoriginalauthororauthors.
#
#LicensedundertheApacheLicense,Version2.0(the"License");
#youmaynotusethisfileexceptincompliancewiththeLicense.
#YoumayobtainacopyoftheLicenseat
#
#https://www.apache.org/licenses/LICENSE-2.0
#
#Unlessrequiredbyapplicablelaworagreedtoinwriting,software
#distributedundertheLicenseisdistributedonan"ASIS"BASIS,
#WITHOUTWARRANTIESORCONDITIONSOFANYKIND,eitherexpressorimplied.
#SeetheLicenseforthespecificlanguagegoverningpermissionsand
#limitationsundertheLicense.
#

##############################################################################
##
##GradlestartupscriptforUN*X
##
##############################################################################

#AttempttosetAPP_HOME
#Resolvelinks:$0maybealink
PRG="$0"
#Needthisforrelativesymlinks.
while[-h"$PRG"];do
ls=`ls-ld"$PRG"`
link=`expr"$ls":'.*->\(.*\)$'`
ifexpr"$link":'/.*'>/dev/null;then
PRG="$link"
else
PRG=`dirname"$PRG"`"/$link"
fi
done
SAVED="`pwd`"
cd"`dirname\"$PRG\"`/">/dev/null
APP_HOME="`pwd-P`"
cd"$SAVED">/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename"$0"`

#AdddefaultJVMoptionshere.YoucanalsouseJAVA_OPTSandGRADLE_OPTStopassJVMoptionstothisscript.
DEFAULT_JVM_OPTS='"-Xmx64m""-Xms64m"'

#Usethemaximumavailable,orsetMAX_FD!=-1tousethatvalue.
MAX_FD="maximum"

warn(){
echo"$*"
}

die(){
echo
echo"$*"
echo
exit1
}

#OSspecificsupport(mustbe'true'or'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case"`uname`"in
CYGWIN*)
cygwin=true
;;
Darwin*)
darwin=true
;;
MINGW*)
msys=true
;;
NONSTOP*)
nonstop=true
;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar


#DeterminetheJavacommandtousetostarttheJVM.
if[-n"$JAVA_HOME"];then
if[-x"$JAVA_HOME/jre/sh/java"];then
#IBM'sJDKonAIXusesstrangelocationsfortheexecutables
JAVACMD="$JAVA_HOME/jre/sh/java"
else
JAVACMD="$JAVA_HOME/bin/java"
fi
if[!-x"$JAVACMD"];then
die"ERROR:JAVA_HOMEissettoaninvaliddirectory:$JAVA_HOME

PleasesettheJAVA_HOMEvariableinyourenvironmenttomatchthe
locationofyourJavainstallation."
fi
else
JAVACMD="java"
whichjava>/dev/null2>&1||die"ERROR:JAVA_HOMEisnotsetandno'java'commandcouldbefoundinyourPATH.

PleasesettheJAVA_HOMEvariableinyourenvironmenttomatchthe
locationofyourJavainstallation."
fi

#Increasethemaximumfiledescriptorsifwecan.
if["$cygwin"="false"-a"$darwin"="false"-a"$nonstop"="false"];then
MAX_FD_LIMIT=`ulimit-H-n`
if[$?-eq0];then
if["$MAX_FD"="maximum"-o"$MAX_FD"="max"];then
MAX_FD="$MAX_FD_LIMIT"
fi
ulimit-n$MAX_FD
if[$?-ne0];then
warn"Couldnotsetmaximumfiledescriptorlimit:$MAX_FD"
fi
else
warn"Couldnotquerymaximumfiledescriptorlimit:$MAX_FD_LIMIT"
fi
fi

#ForDarwin,addoptionstospecifyhowtheapplicationappearsinthedock
if$darwin;then
GRADLE_OPTS="$GRADLE_OPTS\"-Xdock:name=$APP_NAME\"\"-Xdock:icon=$APP_HOME/media/gradle.icns\""
fi

#ForCygwinorMSYS,switchpathstoWindowsformatbeforerunningjava
if["$cygwin"="true"-o"$msys"="true"];then
APP_HOME=`cygpath--path--mixed"$APP_HOME"`
CLASSPATH=`cygpath--path--mixed"$CLASSPATH"`

JAVACMD=`cygpath--unix"$JAVACMD"`

#Webuildthepatternforargumentstobeconvertedviacygpath
ROOTDIRSRAW=`find-L/-maxdepth1-mindepth1-typed2>/dev/null`
SEP=""
fordirin$ROOTDIRSRAW;do
ROOTDIRS="$ROOTDIRS$SEP$dir"
SEP="|"
done
OURCYGPATTERN="(^($ROOTDIRS))"
#Addauser-definedpatterntothecygpatharguments
if["$GRADLE_CYGPATTERN"!=""];then
OURCYGPATTERN="$OURCYGPATTERN|($GRADLE_CYGPATTERN)"
fi
#Nowconvertthearguments-kludgetolimitourselvesto/bin/sh
i=0
forargin"$@";do
CHECK=`echo"$arg"|egrep-c"$OURCYGPATTERN"-`
CHECK2=`echo"$arg"|egrep-c"^-"`###Determineifanoption

if[$CHECK-ne0]&&[$CHECK2-eq0];then###Addedacondition
eval`echoargs$i`=`cygpath--path--ignore--mixed"$arg"`
else
eval`echoargs$i`="\"$arg\""
fi
i=`expr$i+1`
done
case$iin
0)set--;;
1)set--"$args0";;
2)set--"$args0""$args1";;
3)set--"$args0""$args1""$args2";;
4)set--"$args0""$args1""$args2""$args3";;
5)set--"$args0""$args1""$args2""$args3""$args4";;
6)set--"$args0""$args1""$args2""$args3""$args4""$args5";;
7)set--"$args0""$args1""$args2""$args3""$args4""$args5""$args6";;
8)set--"$args0""$args1""$args2""$args3""$args4""$args5""$args6""$args7";;
9)set--"$args0""$args1""$args2""$args3""$args4""$args5""$args6""$args7""$args8";;
esac
fi

#Escapeapplicationargs
save(){
foridoprintf%s\\n"$i"|sed"s/'/'\\\\''/g;1s/^/'/;\$s/\$/'\\\\/";done
echo""
}
APP_ARGS=`save"$@"`

#Collectallargumentsforthejavacommand,followingtheshellquotingandsubstitutionrules
evalset--$DEFAULT_JVM_OPTS$JAVA_OPTS$GRADLE_OPTS"\"-Dorg.gradle.appname=$APP_BASE_NAME\""-classpath"\"$CLASSPATH\""org.gradle.wrapper.GradleWrapperMain"$APP_ARGS"

exec"$JAVACMD""$@"
