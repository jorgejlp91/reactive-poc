FROM node:6.11-slim
WORKDIR /mocks

#Copy mock files
ADD startMocks.sh /mocks/startMocks.sh
ADD endpoint.yaml /mocks/endpoint.yaml
RUN chmod +x /mocks/startMocks.sh

#Install stubby4node
RUN npm install -g stubby

#Expose ports
EXPOSE 8084 8907

#RUN Mocks
ENTRYPOINT ["./startMocks.sh"]
