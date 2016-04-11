QT += core
QT -= gui

CONFIG += c++11

TARGET = ComputerVisionQT
CONFIG += console
CONFIG -= app_bundle

TEMPLATE = app

SOURCES += main.cpp

INCLUDEPATH += C:\\open\\dist\\install\\include
LIBS += -LC:\\open\\dist\\bin \
    -llibopencv_core310 \
    -llibopencv_highgui310 \
    -llibopencv_imgcodecs310 \
    -llibopencv_imgproc310 \
    -llibopencv_features2d310 \
    -llibopencv_calib3d310
