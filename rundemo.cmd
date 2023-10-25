REM set LANGUAGE_KEY=0ae8f180c032498a9829c582a83dd797
REM set LANGUAGE_ENDPOINT=https://ignite-textsummary.cognitiveservices.azure.com/
set LANGUAGE_KEY=2b67712e4ac64397b242c7e138335428
set LANGUAGE_ENDPOINT=https://ignite-textsummary-eastus.cognitiveservices.azure.com/
set AZURE_OPEN_API_ENDPOINT=https://build2023.openai.azure.com/
set AZURE_OPEN_API_KEY=ad28bbaeea1b49828c81910e9cbd5d8b
set AZURE_OPEN_API_MODEL_DEPLOYMENT_NAME=test
set AZURE_STORAGE_CONNECTION_STRING=DefaultEndpointsProtocol=https;AccountName=ignitewebjob;AccountKey=X+JqpubLLFKrzqRm+/qXNB+f3mbHI8x1MnjZYTg0ZJiVB0Ykr3IDJ2lrNO0KQsbg6XkXOxJqunHa+AStlrFJwA==;EndpointSuffix=core.windows.net
set AZURE_STORAGE_DESTINATION_QUEUE_NAME=destinationq
set AZURE_STORAGE_SOURCE_QUEUE_NAME=sourceq
set WEBSITES_ENABLE_APP_SERVICE_STORAGE=true
set XDT_MicrosoftApplicationInsights_Mode=default

:start
java -jar java-openai.jar
waitfor "test" /T 3
goto :start
