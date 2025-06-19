#!/bin/bash

# Safety check
if [ ! -d "frontend/ClockMate" ] || [ ! -f "pom.xml" ]; then
  echo "❌ You must run this from the top-level ClockMate folder."
  exit 1
fi
echo "📁 Moving backend files to 'backend/'..."
mkdir -p backend
mv HELP.md mvnw mvnw.cmd pom.xml src target .gitignore backend/ 2>/dev/null

echo "📁 Flattening frontend folder..."
mv frontend/ClockMate/* frontend/ 2>/dev/null
mv frontend/ClockMate/.* frontend/ 2>/dev/null || true

echo "🧹 Cleaning up nested frontend/ClockMate..."
rm -rf frontend/ClockMate

echo "✅ Project structure reorganized successfully!"
