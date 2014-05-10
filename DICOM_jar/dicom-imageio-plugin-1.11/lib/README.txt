dicom.jar is the full plugin.

dicom.stripped.jar :
- includes a data dictionary that does not have tag names.
- includes a Tag class that does not contain the definition of all constants.
- is compressed.

dicom.stripped.jar is not suitable for compilation. It can be useful
for runtime for those applications that does not need to display attribute
names and can benefit from a smaller jar file.
