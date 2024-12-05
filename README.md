# Description

Folowing example is using [LangChain4j](https://docs.langchain4j.dev/) to build a simple RAG system.

# Setup

## Ollama local client
1. Install Ollama locally https://ollama.com
2. Download LLM `ollama pull llama3.2`
3. Download embedding model `ollama pull mxbai-embed-large`

## QDrant DB
1. Using `docker-compose` start your local QDrant instance
2. Init a new collection with a dimension of 1024
3. Make sure [application.yml](src/main/resources/application.yml) points to the initialized collection

# Runtime
## Init vector store 
Use [DocumentsReader.kt](src/main/kotlin/com/iteratec/words2wisdom/documents/DocumentsReader.kt) to init collection with pdf content.

## Question API
Use question API to explore 40k universe with power of a RAG
```
curl http://localhost:8080/question/ask  -H 'Content-Type: application/json' -d '{
  "question": "Who is the God Emperror?"
}'
```
## Ollama API
### Embeddings endpoint
```
curl http://localhost:11434/api/embeddings -d '{
  "model": "mxbai-embed-large",
  "prompt": "The sky is blue because of Rayleigh scattering"
}'
```

