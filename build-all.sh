#!/bin/bash

# Build all services

set -e

SERVICES_DIR="services"

echo "Building all services..."

for service_dir in $SERVICES_DIR/*/; do
    service_name=$(basename "$service_dir")
    
    if [ -f "$service_dir/pom.xml" ]; then
        echo "Building $service_name..."
        cd "$service_dir"
        ./mvnw clean package -DskipTests -q
        if [ $? -eq 0 ]; then
            echo "$service_name: OK"
        else
            echo "$service_name: FAILED"
            exit 1
        fi
        cd - > /dev/null
    fi
done

echo "Done."
