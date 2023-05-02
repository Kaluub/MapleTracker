@rem
@remCopyright2015theoriginalauthororauthors.
@rem
@remLicensedundertheApacheLicense,Version2.0(the"License");
@remyoumaynotusethisfileexceptincompliancewiththeLicense.
@remYoumayobtainacopyoftheLicenseat
@rem
@remhttps://www.apache.org/licenses/LICENSE-2.0
@rem
@remUnlessrequiredbyapplicablelaworagreedtoinwriting,software
@remdistributedundertheLicenseisdistributedonan"ASIS"BASIS,
@remWITHOUTWARRANTIESORCONDITIONSOFANYKIND,eitherexpressorimplied.
@remSeetheLicenseforthespecificlanguagegoverningpermissionsand
@remlimitationsundertheLicense.
@rem

@if"%DEBUG%"==""@echooff
@rem##########################################################################
@rem
@remGradlestartupscriptforWindows
@rem
@rem##########################################################################

@remSetlocalscopeforthevariableswithwindowsNTshell
if"%OS%"=="Windows_NT"setlocal

setDIRNAME=%~dp0
if"%DIRNAME%"==""setDIRNAME=.
setAPP_BASE_NAME=%~n0
setAPP_HOME=%DIRNAME%

@remResolveany"."and".."inAPP_HOMEtomakeitshorter.
for%%iin("%APP_HOME%")dosetAPP_HOME=%%~fi

@remAdddefaultJVMoptionshere.YoucanalsouseJAVA_OPTSandGRADLE_OPTStopassJVMoptionstothisscript.
setDEFAULT_JVM_OPTS="-Xmx64m""-Xms64m"

@remFindjava.exe
ifdefinedJAVA_HOMEgotofindJavaFromJavaHome

setJAVA_EXE=java.exe
%JAVA_EXE%-version>NUL2>&1
if"%ERRORLEVEL%"=="0"gotoexecute

echo.
echoERROR:JAVA_HOMEisnotsetandno'java'commandcouldbefoundinyourPATH.
echo.
echoPleasesettheJAVA_HOMEvariableinyourenvironmenttomatchthe
echolocationofyourJavainstallation.

gotofail

:findJavaFromJavaHome
setJAVA_HOME=%JAVA_HOME:"=%
setJAVA_EXE=%JAVA_HOME%/bin/java.exe

ifexist"%JAVA_EXE%"gotoexecute

echo.
echoERROR:JAVA_HOMEissettoaninvaliddirectory:%JAVA_HOME%
echo.
echoPleasesettheJAVA_HOMEvariableinyourenvironmenttomatchthe
echolocationofyourJavainstallation.

gotofail

:execute
@remSetupthecommandline

setCLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar


@remExecuteGradle
"%JAVA_EXE%"%DEFAULT_JVM_OPTS%%JAVA_OPTS%%GRADLE_OPTS%"-Dorg.gradle.appname=%APP_BASE_NAME%"-classpath"%CLASSPATH%"org.gradle.wrapper.GradleWrapperMain%*

:end
@remEndlocalscopeforthevariableswithwindowsNTshell
if"%ERRORLEVEL%"=="0"gotomainEnd

:fail
remSetvariableGRADLE_EXIT_CONSOLEifyouneedthe_script_returncodeinsteadof
remthe_cmd.exe/c_returncode!
ifnot""=="%GRADLE_EXIT_CONSOLE%"exit1
exit/b1

:mainEnd
if"%OS%"=="Windows_NT"endlocal

:omega
