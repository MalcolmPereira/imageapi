Write-Output "Starting Standalone Docker Run"

#Run RabbitMQ with AMQP 1.0 Image
Set-Location rabbitmq-docker
./rundocker.ps1
Set-Location ..

#Run PostGreSQL Image
Set-Location postgresql-docker
./rundocker.ps1
Set-Location ..

#Run Jaegar Tracing
Set-Location jaegar-tracing
./run-jaegar-docker.ps1
Set-Location ..

#Run Image Storage
Set-Location imagestorage
./rundocker.ps1
Set-Location ..

#Run Image Thumbnail
Set-Location imagethumbnail
./rundocker.ps1
Set-Location ..

#Run Image Validation
Set-Location imagevalidation
./rundocker.ps1
Set-Location ..

#Run Image API
Set-Location imageapi
./rundocker.ps1
Set-Location ..

#Run Image Client
Set-Location imageclient
./rundocker.ps1
Set-Location ..

Write-Output "Done Docker Standalone Run "