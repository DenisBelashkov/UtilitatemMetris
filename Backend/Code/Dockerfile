FROM python:3.8-alpine
#WORKDIR /backend
#ENV FLASK_APP=backend/app.py
#ENV FLASK_RUN_HOST=0.0.0.0
#RUN apk add --no-cache gcc musl-dev linux-headers
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
#EXPOSE 5000
COPY .. .
CMD ["python", "main.py"]
