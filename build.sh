# run script from root of the directroy of this project.
#####build java docker image #########


docker build -t java_app .
docker run -it java_app
