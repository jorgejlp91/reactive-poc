#!/bin/bash
awk 'FNR==1{print ""}1' endpoint.yaml > endpoints-mock.yaml
stubby -d endpoints-mock.yaml -s 8084 -t 7449 -a 8907 -w
rm endpoints-mock.yaml
