/**
 **   __ __|_  ___________________________________________________________________________  ___|__ __
 **  //    /\                                           _                                  /\    \\  
 ** //____/  \__     __ _____ _____ _____ _____ _____  | |     __ _____ _____ __        __/  \____\\ 
 **  \    \  / /  __|  |     |   __|  _  |     |  _  | | |  __|  |     |   __|  |      /\ \  /    /  
 **   \____\/_/  |  |  |  |  |  |  |     | | | |   __| | | |  |  |  |  |  |  |  |__   "  \_\/____/   
 **  /\    \     |_____|_____|_____|__|__|_|_|_|__|    | | |_____|_____|_____|_____|  _  /    /\     
 ** /  \____\                       http://jogamp.org  |_|                              /____/  \    
 ** \  /   "' _________________________________________________________________________ `"   \  /    
 **  \/____.                                                                             .____\/     
 **
 ** Postprocessing filter implementing a simple sepia cross-processing.
 **
 **/

uniform sampler2D sampler0;

const vec3 sepia = vec3(1.2, 1.0, 0.8);
const vec3 ntsc = vec3(0.299, 0.587, 0.114);
 
void main(void) {
    vec4 sample;
    sample = texture2D(sampler0, gl_TexCoord[0].st);
    //convert to grayscale using NTSC conversion weights
    float gray = dot(sample.rgb, ntsc);
    gl_FragColor = vec4(vec3(gray) * sepia, 1.0);
}