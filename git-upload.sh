#!/bin/bash

# Script para subir cambios a un repositorio Git

# Verificar si estamos en un repositorio Git
if ! git rev-parse --is-inside-work-tree > /dev/null 2>&1; then
    echo "Error: No estás en un repositorio Git."
    echo "Por favor, ejecuta este script desde un directorio con un repositorio Git."
    exit 1
fi

# Obtener el estado del repositorio
status=$(git status --porcelain)

# Verificar si hay cambios para commitear
if [ -z "$status" ]; then
    echo "No hay cambios para commitear."
    exit 0
fi

# Mostrar los cambios
echo "Cambios detectados:"
git status -s

# Pedir mensaje del commit
read -p "Ingresa el mensaje para el commit: " commit_message

# Verificar si se ingresó un mensaje
if [ -z "$commit_message" ]; then
    echo "Error: Debes ingresar un mensaje para el commit."
    exit 1
fi

# Ejecutar los comandos Git
git add .
git commit -m "$commit_message"
git push

echo "Cambios subidos exitosamente al repositorio."