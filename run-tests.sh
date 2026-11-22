#!/bin/bash

 set -e

echo "Starting Java tests build..."

cd "$(dirname "$0")"

if [ ! -x gradlew ]; then
    echo "Making gradlew executable..."
    chmod +x gradlew
fi
echo "Clearing previous allure-results..."
rm -rf -- allure-results

echo "Cleaning previous builds and running tests..."
./gradlew clean test

echo "Build completed successfully!"
echo "Allure results saved to: allure-results/"