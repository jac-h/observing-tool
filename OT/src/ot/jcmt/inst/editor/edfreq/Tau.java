/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor.edfreq;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class Tau
{

   double [][] atmosphere = {

     { 3.385000e+02,   1.582000e-01 },
     { 3.394000e+02,   2.026000e-01 },
     { 3.403000e+02,   2.435000e-01 },
     { 3.413000e+02,   2.750000e-01 },
     { 3.422000e+02,   2.611000e-01 },
     { 3.431000e+02,   2.280000e-01 },
     { 3.440000e+02,   2.382000e-01 },
     { 3.449000e+02,   2.384000e-01 },
     { 3.458000e+02,   1.450000e-01 },
     { 3.467000e+02,   2.602000e-02 },
     { 3.477000e+02,   6.453000e-04 },
     { 3.486000e+02,   2.880000e-02 },
     { 3.495000e+02,   7.430000e-02 },
     { 3.504000e+02,   1.950000e-01 },
     { 3.513000e+02,   3.283000e-01 },
     { 3.522000e+02,   3.409000e-01 },
     { 3.531000e+02,   2.363000e-01 },
     { 3.541000e+02,   1.457000e-01 },
     { 3.550000e+02,   1.450000e-01 },
     { 3.559000e+02,   1.801000e-01 },
     { 3.568000e+02,   2.323000e-01 },
     { 3.577000e+02,   3.287000e-01 },
     { 3.586000e+02,   4.233000e-01 },
     { 3.596000e+02,   4.413000e-01 },
     { 3.605000e+02,   3.876000e-01 },
     { 3.614000e+02,   3.512000e-01 },
     { 3.623000e+02,   4.006000e-01 },
     { 3.632000e+02,   4.738000e-01 },
     { 3.641000e+02,   4.704000e-01 },
     { 3.650000e+02,   4.164000e-01 },
     { 3.660000e+02,   4.519000e-01 },
     { 3.669000e+02,   6.144000e-01 },
     { 3.678000e+02,   8.020000e-01 },
     { 3.687000e+02,   8.703000e-01 },
     { 3.696000e+02,   8.059000e-01 },
     { 3.705000e+02,   7.590000e-01 },
     { 3.714000e+02,   8.004000e-01 },
     { 3.724000e+02,   9.141000e-01 },
     { 3.733000e+02,   1.056000e+00 },
     { 3.742000e+02,   1.234000e+00 },
     { 3.751000e+02,   1.532000e+00 },
     { 3.760000e+02,   2.015000e+00 },
     { 3.769000e+02,   2.711000e+00 },
     { 3.779000e+02,   3.728000e+00 },
     { 3.788000e+02,   5.026000e+00 },
     { 3.797000e+02,   5.291000e+00 },
     { 3.806000e+02,   4.970000e+00 },
     { 3.815000e+02,   4.545000e+00 },
     { 3.824000e+02,   3.530000e+00 },
     { 3.833000e+02,   2.575000e+00 },
     { 3.843000e+02,   1.978000e+00 },
     { 3.852000e+02,   1.632000e+00 },
     { 3.861000e+02,   1.395000e+00 },
     { 3.870000e+02,   1.193000e+00 },
     { 3.879000e+02,   1.004000e+00 },
     { 3.888000e+02,   8.144000e-01 },
     { 3.897000e+02,   6.508000e-01 },
     { 3.907000e+02,   5.697000e-01 },
     { 3.916000e+02,   5.621000e-01 },
     { 3.925000e+02,   5.646000e-01 },
     { 3.934000e+02,   5.724000e-01 },
     { 3.943000e+02,   6.001000e-01 },
     { 3.952000e+02,   6.008000e-01 },
     { 3.961000e+02,   5.260000e-01 },
     { 3.971000e+02,   4.036000e-01 },
     { 3.980000e+02,   3.126000e-01 },
     { 3.989000e+02,   3.050000e-01 },
     { 3.998000e+02,   3.744000e-01 },
     { 4.007000e+02,   4.606000e-01 },
     { 4.016000e+02,   4.831000e-01 },
     { 4.026000e+02,   4.293000e-01 },
     { 4.035000e+02,   3.733000e-01 },
     { 4.044000e+02,   3.704000e-01 },
     { 4.053000e+02,   3.887000e-01 },
     { 4.062000e+02,   3.946000e-01 },
     { 4.071000e+02,   4.017000e-01 },
     { 4.080000e+02,   4.117000e-01 },
     { 4.090000e+02,   4.021000e-01 },
     { 4.099000e+02,   3.835000e-01 },
     { 4.108000e+02,   3.948000e-01 },
     { 4.117000e+02,   4.363000e-01 },
     { 4.126000e+02,   4.603000e-01 },
     { 4.135000e+02,   4.309000e-01 },
     { 4.144000e+02,   3.689000e-01 },
     { 4.154000e+02,   3.357000e-01 },
     { 4.163000e+02,   3.485000e-01 },
     { 4.172000e+02,   3.664000e-01 },
     { 4.181000e+02,   3.777000e-01 },
     { 4.190000e+02,   4.161000e-01 },
     { 4.199000e+02,   5.061000e-01 },
     { 4.209000e+02,   6.707000e-01 },
     { 4.218000e+02,   9.680000e-01 },
     { 4.227000e+02,   1.469000e+00 },
     { 4.236000e+02,   2.250000e+00 },
     { 4.245000e+02,   3.114000e+00 },
     { 4.254000e+02,   2.575000e+00 },
     { 4.263000e+02,   1.628000e+00 },
     { 4.273000e+02,   1.034000e+00 },
     { 4.282000e+02,   7.448000e-01 },
     { 4.291000e+02,   6.535000e-01 },
     { 4.300000e+02,   6.249000e-01 },
     { 4.309000e+02,   6.074000e-01 },
     { 4.318000e+02,   6.330000e-01 },
     { 4.327000e+02,   6.979000e-01 },
     { 4.337000e+02,   7.466000e-01 },
     { 4.346000e+02,   7.536000e-01 },
     { 4.355000e+02,   7.630000e-01 },
     { 4.364000e+02,   8.761000e-01 },
     { 4.373000e+02,   1.182000e+00 },
     { 4.382000e+02,   1.627000e+00 },
     { 4.391000e+02,   1.912000e+00 },
     { 4.401000e+02,   1.840000e+00 },
     { 4.410000e+02,   1.711000e+00 },
     { 4.419000e+02,   1.768000e+00 },
     { 4.428000e+02,   2.023000e+00 },
     { 4.437000e+02,   2.441000e+00 },
     { 4.446000e+02,   3.065000e+00 },
     { 4.456000e+02,   4.039000e+00 },
     { 4.465000e+02,   5.375000e+00 },
     { 4.474000e+02,   5.461000e+00 },
     { 4.483000e+02,   5.019000e+00 },
     { 4.492000e+02,   4.695000e+00 },
     { 4.501000e+02,   3.780000e+00 },
     { 4.510000e+02,   2.852000e+00 },
     { 4.520000e+02,   2.218000e+00 },
     { 4.529000e+02,   1.802000e+00 },
     { 4.538000e+02,   1.509000e+00 },
     { 4.547000e+02,   1.272000e+00 },
     { 4.556000e+02,   1.071000e+00 },
     { 4.565000e+02,   9.315000e-01 },
     { 4.574000e+02,   8.511000e-01 },
     { 4.584000e+02,   7.891000e-01 },
     { 4.593000e+02,   7.354000e-01 },
     { 4.602000e+02,   7.171000e-01 },
     { 4.611000e+02,   7.349000e-01 },
     { 4.620000e+02,   7.616000e-01 },
     { 4.629000e+02,   7.810000e-01 },
     { 4.639000e+02,   7.811000e-01 },
     { 4.648000e+02,   7.500000e-01 },
     { 4.657000e+02,   7.014000e-01 },
     { 4.666000e+02,   6.808000e-01 },
     { 4.675000e+02,   7.304000e-01 },
     { 4.684000e+02,   8.502000e-01 },
     { 4.693000e+02,   1.005000e+00 },
     { 4.703000e+02,   1.149000e+00 },
     { 4.712000e+02,   1.263000e+00 },
     { 4.721000e+02,   1.423000e+00 },
     { 4.730000e+02,   1.735000e+00 },
     { 4.739000e+02,   2.187000e+00 },
     { 4.748000e+02,   2.339000e+00 },
     { 4.757000e+02,   1.884000e+00 },
     { 4.767000e+02,   1.364000e+00 },
     { 4.776000e+02,   1.000000e+00 },
     { 4.785000e+02,   8.066000e-01 },
     { 4.794000e+02,   7.452000e-01 },
     { 4.803000e+02,   7.504000e-01 },
     { 4.812000e+02,   7.982000e-01 },
     { 4.821000e+02,   8.772000e-01 },
     { 4.831000e+02,   9.547000e-01 },
     { 4.840000e+02,   1.055000e+00 },
     { 4.849000e+02,   1.286000e+00 },
     { 4.858000e+02,   1.761000e+00 },
     { 4.867000e+02,   2.411000e+00 },
     { 4.876000e+02,   2.522000e+00 },
     { 4.886000e+02,   1.915000e+00 },
     { 4.895000e+02,   1.398000e+00 },
     { 4.904000e+02,   1.128000e+00 },
     { 4.913000e+02,   1.040000e+00 },
     { 4.922000e+02,   1.022000e+00 },
     { 4.931000e+02,   9.974000e-01 },
     { 4.940000e+02,   9.648000e-01 },
     { 4.950000e+02,   9.533000e-01 },
     { 4.959000e+02,   9.640000e-01 },
     { 4.968000e+02,   9.812000e-01 },
     { 4.977000e+02,   1.002000e+00 },
     { 4.986000e+02,   1.024000e+00 },
     { 4.995000e+02,   1.047000e+00 },
     { 5.004000e+02,   1.084000e+00 },
     { 5.014000e+02,   1.149000e+00 },
     { 5.023000e+02,   1.223000e+00 },
     { 5.032000e+02,   1.267000e+00 },
     { 5.041000e+02,   1.263000e+00 },
     { 5.050000e+02,   1.243000e+00 },
     { 5.059000e+02,   1.265000e+00 },
     { 5.069000e+02,   1.343000e+00 },
     { 5.078000e+02,   1.421000e+00 },
     { 5.087000e+02,   1.448000e+00 },
     { 5.096000e+02,   1.436000e+00 },
     { 5.105000e+02,   1.431000e+00 },
     { 5.114000e+02,   1.460000e+00 },
     { 5.123000e+02,   1.525000e+00 },
     { 5.133000e+02,   1.601000e+00 },
     { 5.142000e+02,   1.659000e+00 },
     { 5.151000e+02,   1.696000e+00 },
     { 5.160000e+02,   1.746000e+00 },
     { 5.169000e+02,   1.837000e+00 },
     { 5.178000e+02,   1.955000e+00 },
     { 5.187000e+02,   2.070000e+00 },
     { 5.197000e+02,   2.161000e+00 },
     { 5.206000e+02,   2.230000e+00 },
     { 5.215000e+02,   2.304000e+00 },
     { 5.224000e+02,   2.403000e+00 },
     { 5.233000e+02,   2.517000e+00 },
     { 5.242000e+02,   2.632000e+00 },
     { 5.251000e+02,   2.762000e+00 },
     { 5.261000e+02,   2.924000e+00 },
     { 5.270000e+02,   3.099000e+00 },
     { 5.279000e+02,   3.253000e+00 },
     { 5.288000e+02,   3.388000e+00 },
     { 5.297000e+02,   3.540000e+00 },
     { 5.306000e+02,   3.724000e+00 },
     { 5.316000e+02,   3.890000e+00 },
     { 5.325000e+02,   4.004000e+00 },
     { 5.334000e+02,   4.116000e+00 },
     { 5.343000e+02,   4.304000e+00 },
     { 5.352000e+02,   4.612000e+00 },
     { 5.361000e+02,   4.965000e+00 },
     { 5.370000e+02,   5.140000e+00 },
     { 5.380000e+02,   5.030000e+00 },
     { 5.389000e+02,   4.928000e+00 },
     { 5.398000e+02,   5.007000e+00 },
     { 5.407000e+02,   5.170000e+00 },
     { 5.416000e+02,   5.264000e+00 },
     { 5.425000e+02,   5.376000e+00 },
     { 5.434000e+02,   5.610000e+00 },
     { 5.444000e+02,   5.822000e+00 },
     { 5.453000e+02,   5.883000e+00 },
     { 5.462000e+02,   5.923000e+00 },
     { 5.471000e+02,   5.969000e+00 },
     { 5.480000e+02,   5.928000e+00 },
     { 5.489000e+02,   5.742000e+00 },
     { 5.499000e+02,   5.338000e+00 },
     { 5.508000e+02,   4.936000e+00 },
     { 5.517000e+02,   4.803000e+00 },
     { 5.526000e+02,   4.924000e+00 },
     { 5.535000e+02,   4.961000e+00 },
     { 5.544000e+02,   4.608000e+00 },
     { 5.553000e+02,   4.244000e+00 },
     { 5.563000e+02,   4.124000e+00 },
     { 5.572000e+02,   4.214000e+00 },
     { 5.581000e+02,   4.380000e+00 },
     { 5.590000e+02,   4.567000e+00 },
     { 5.599000e+02,   4.775000e+00 },
     { 5.608000e+02,   4.894000e+00 },
     { 5.617000e+02,   4.893000e+00 },
     { 5.627000e+02,   4.910000e+00 },
     { 5.636000e+02,   5.004000e+00 },
     { 5.645000e+02,   5.056000e+00 },
     { 5.654000e+02,   4.975000e+00 },
     { 5.663000e+02,   4.881000e+00 },
     { 5.672000e+02,   4.931000e+00 },
     { 5.681000e+02,   5.144000e+00 },
     { 5.691000e+02,   5.321000e+00 },
     { 5.700000e+02,   5.225000e+00 },
     { 5.709000e+02,   5.019000e+00 },
     { 5.718000e+02,   4.921000e+00 },
     { 5.727000e+02,   4.924000e+00 },
     { 5.736000e+02,   4.905000e+00 },
     { 5.746000e+02,   4.809000e+00 },
     { 5.755000e+02,   4.701000e+00 },
     { 5.764000e+02,   4.674000e+00 },
     { 5.773000e+02,   4.748000e+00 },
     { 5.782000e+02,   4.856000e+00 },
     { 5.791000e+02,   4.875000e+00 },
     { 5.800000e+02,   4.736000e+00 },
     { 5.810000e+02,   4.485000e+00 },
     { 5.819000e+02,   4.249000e+00 },
     { 5.828000e+02,   4.126000e+00 },
     { 5.837000e+02,   4.099000e+00 },
     { 5.846000e+02,   4.034000e+00 },
     { 5.855000e+02,   3.831000e+00 },
     { 5.864000e+02,   3.584000e+00 },
     { 5.874000e+02,   3.413000e+00 },
     { 5.883000e+02,   3.324000e+00 },
     { 5.892000e+02,   3.256000e+00 },
     { 5.901000e+02,   3.168000e+00 },
     { 5.910000e+02,   3.063000e+00 },
     { 5.919000e+02,   2.946000e+00 },
     { 5.929000e+02,   2.831000e+00 },
     { 5.938000e+02,   2.746000e+00 },
     { 5.947000e+02,   2.689000e+00 },
     { 5.956000e+02,   2.617000e+00 },
     { 5.965000e+02,   2.520000e+00 },
     { 5.974000e+02,   2.435000e+00 },
     { 5.983000e+02,   2.377000e+00 },
     { 5.993000e+02,   2.314000e+00 },
     { 6.002000e+02,   2.235000e+00 },
     { 6.011000e+02,   2.169000e+00 },
     { 6.020000e+02,   2.129000e+00 },
     { 6.029000e+02,   2.081000e+00 },
     { 6.038000e+02,   2.010000e+00 },
     { 6.047000e+02,   1.940000e+00 },
     { 6.057000e+02,   1.900000e+00 },
     { 6.066000e+02,   1.880000e+00 },
     { 6.075000e+02,   1.858000e+00 },
     { 6.084000e+02,   1.838000e+00 },
     { 6.093000e+02,   1.836000e+00 },
     { 6.102000e+02,   1.840000e+00 },
     { 6.111000e+02,   1.820000e+00 },
     { 6.121000e+02,   1.793000e+00 },
     { 6.130000e+02,   1.817000e+00 },
     { 6.139000e+02,   1.903000e+00 },
     { 6.148000e+02,   2.024000e+00 },
     { 6.157000e+02,   2.195000e+00 },
     { 6.166000e+02,   2.481000e+00 },
     { 6.176000e+02,   2.944000e+00 },
     { 6.185000e+02,   3.618000e+00 },
     { 6.194000e+02,   4.429000e+00 },
     { 6.203000e+02,   4.977000e+00 },
     { 6.212000e+02,   4.811000e+00 },
     { 6.221000e+02,   4.039000e+00 },
     { 6.230000e+02,   3.206000e+00 },
     { 6.240000e+02,   2.591000e+00 },
     { 6.249000e+02,   2.176000e+00 },
     { 6.258000e+02,   1.898000e+00 },
     { 6.267000e+02,   1.713000e+00 },
     { 6.276000e+02,   1.584000e+00 },
     { 6.285000e+02,   1.483000e+00 },
     { 6.294000e+02,   1.406000e+00 },
     { 6.304000e+02,   1.360000e+00 },
     { 6.313000e+02,   1.331000e+00 },
     { 6.322000e+02,   1.297000e+00 },
     { 6.331000e+02,   1.254000e+00 },
     { 6.340000e+02,   1.218000e+00 },
     { 6.349000e+02,   1.194000e+00 },
     { 6.359000e+02,   1.175000e+00 },
     { 6.368000e+02,   1.158000e+00 },
     { 6.377000e+02,   1.141000e+00 },
     { 6.386000e+02,   1.125000e+00 },
     { 6.395000e+02,   1.106000e+00 },
     { 6.404000e+02,   1.093000e+00 },
     { 6.413000e+02,   1.090000e+00 },
     { 6.423000e+02,   1.088000e+00 },
     { 6.432000e+02,   1.074000e+00 },
     { 6.441000e+02,   1.058000e+00 },
     { 6.450000e+02,   1.060000e+00 },
     { 6.459000e+02,   1.079000e+00 },
     { 6.468000e+02,   1.086000e+00 },
     { 6.477000e+02,   1.069000e+00 },
     { 6.487000e+02,   1.044000e+00 },
     { 6.496000e+02,   1.041000e+00 },
     { 6.505000e+02,   1.052000e+00 },
     { 6.514000e+02,   1.052000e+00 },
     { 6.523000e+02,   1.049000e+00 },
     { 6.532000e+02,   1.089000e+00 },
     { 6.541000e+02,   1.189000e+00 },
     { 6.551000e+02,   1.294000e+00 },
     { 6.560000e+02,   1.336000e+00 },
     { 6.569000e+02,   1.309000e+00 },
     { 6.578000e+02,   1.243000e+00 },
     { 6.587000e+02,   1.157000e+00 },
     { 6.596000e+02,   1.081000e+00 },
     { 6.606000e+02,   1.044000e+00 },
     { 6.615000e+02,   1.027000e+00 },
     { 6.624000e+02,   1.002000e+00 },
     { 6.633000e+02,   9.803000e-01 },
     { 6.642000e+02,   9.829000e-01 },
     { 6.651000e+02,   9.958000e-01 },
     { 6.660000e+02,   9.898000e-01 },
     { 6.670000e+02,   9.725000e-01 },
     { 6.679000e+02,   9.678000e-01 },
     { 6.688000e+02,   9.789000e-01 },
     { 6.697000e+02,   9.883000e-01 },
     { 6.706000e+02,   9.868000e-01 },
     { 6.715000e+02,   9.831000e-01 },
     { 6.724000e+02,   9.857000e-01 },
     { 6.734000e+02,   9.876000e-01 },
     { 6.743000e+02,   9.814000e-01 },
     { 6.752000e+02,   9.751000e-01 },
     { 6.761000e+02,   9.777000e-01 },
     { 6.770000e+02,   9.876000e-01 },
     { 6.779000e+02,   9.976000e-01 },
     { 6.789000e+02,   1.009000e+00 },
     { 6.798000e+02,   1.023000e+00 },
     { 6.807000e+02,   1.034000e+00 },
     { 6.816000e+02,   1.039000e+00 },
     { 6.825000e+02,   1.040000e+00 },
     { 6.834000e+02,   1.043000e+00 },
     { 6.843000e+02,   1.055000e+00 },
     { 6.853000e+02,   1.071000e+00 },
     { 6.862000e+02,   1.075000e+00 },
     { 6.871000e+02,   1.065000e+00 },
     { 6.880000e+02,   1.061000e+00 },
     { 6.889000e+02,   1.082000e+00 },
     { 6.898000e+02,   1.124000e+00 },
     { 6.907000e+02,   1.163000e+00 },
     { 6.917000e+02,   1.188000e+00 },
     { 6.926000e+02,   1.200000e+00 },
     { 6.935000e+02,   1.200000e+00 },
     { 6.944000e+02,   1.186000e+00 },
     { 6.953000e+02,   1.169000e+00 },
     { 6.962000e+02,   1.170000e+00 },
     { 6.971000e+02,   1.197000e+00 },
     { 6.981000e+02,   1.222000e+00 },
     { 6.990000e+02,   1.227000e+00 },
     { 6.999000e+02,   1.237000e+00 },
     { 7.008000e+02,   1.278000e+00 },
     { 7.017000e+02,   1.321000e+00 },
     { 7.026000e+02,   1.340000e+00 },
     { 7.036000e+02,   1.354000e+00 },
     { 7.045000e+02,   1.386000e+00 },
     { 7.054000e+02,   1.418000e+00 },
     { 7.063000e+02,   1.435000e+00 },
     { 7.072000e+02,   1.464000e+00 },
     { 7.081000e+02,   1.521000e+00 },
     { 7.090000e+02,   1.577000e+00 },
     { 7.100000e+02,   1.614000e+00 },
     { 7.109000e+02,   1.647000e+00 },
     { 7.118000e+02,   1.715000e+00 },
     { 7.127000e+02,   1.867000e+00 },
     { 7.136000e+02,   2.160000e+00 },
     { 7.145000e+02,   2.558000e+00 },
     { 7.154000e+02,   2.761000e+00 },
     { 7.164000e+02,   2.559000e+00 },
     { 7.173000e+02,   2.302000e+00 },
     { 7.182000e+02,   2.206000e+00 },
     { 7.191000e+02,   2.237000e+00 },
     { 7.200000e+02,   2.296000e+00 },
     { 7.209000e+02,   2.354000e+00 },
     { 7.219000e+02,   2.445000e+00 },
     { 7.228000e+02,   2.565000e+00 },
     { 7.237000e+02,   2.677000e+00 },
     { 7.246000e+02,   2.777000e+00 },
     { 7.255000e+02,   2.902000e+00 },
     { 7.264000e+02,   3.049000e+00 },
     { 7.273000e+02,   3.197000e+00 },
     { 7.283000e+02,   3.355000e+00 },
     { 7.292000e+02,   3.538000e+00 },
     { 7.301000e+02,   3.714000e+00 },
     { 7.310000e+02,   3.860000e+00 },
     { 7.319000e+02,   4.007000e+00 },
     { 7.328000e+02,   4.185000e+00 },
     { 7.337000e+02,   4.384000e+00 },
     { 7.347000e+02,   4.592000e+00 },
     { 7.356000e+02,   4.837000e+00 },
     { 7.365000e+02,   5.086000e+00 },
     { 7.374000e+02,   5.171000e+00 },
     { 7.383000e+02,   5.063000e+00 },
     { 7.392000e+02,   5.013000e+00 },
     { 7.401000e+02,   5.147000e+00 },
     { 7.411000e+02,   5.297000e+00 },
     { 7.420000e+02,   5.214000e+00 },
     { 7.429000e+02,   5.047000e+00 },
     { 7.438000e+02,   5.025000e+00 },
     { 7.447000e+02,   5.123000e+00 },
     { 7.456000e+02,   5.162000e+00 },
     { 7.466000e+02,   5.074000e+00 },
     { 7.475000e+02,   4.977000e+00 },
     { 7.484000e+02,   4.947000e+00 },
     { 7.493000e+02,   4.954000e+00 },
     { 7.502000e+02,   4.955000e+00 },
     { 7.511000e+02,   4.950000e+00 },
     { 7.520000e+02,   4.971000e+00 },
     { 7.530000e+02,   5.029000e+00 },
     { 7.539000e+02,   5.131000e+00 },
     { 7.548000e+02,   5.312000e+00 },
     { 7.557000e+02,   5.575000e+00 },
     { 7.566000e+02,   5.819000e+00 },
     { 7.575000e+02,   5.969000e+00 },
     { 7.584000e+02,   6.110000e+00 },
     { 7.594000e+02,   6.213000e+00 },
     { 7.603000e+02,   6.018000e+00 },
     { 7.612000e+02,   5.652000e+00 },
     { 7.621000e+02,   5.407000e+00 },
     { 7.630000e+02,   5.321000e+00 },
     { 7.639000e+02,   5.295000e+00 },
     { 7.649000e+02,   5.237000e+00 },
     { 7.658000e+02,   5.159000e+00 },
     { 7.667000e+02,   5.127000e+00 },
     { 7.676000e+02,   5.141000e+00 },
     { 7.685000e+02,   5.080000e+00 },
     { 7.694000e+02,   4.870000e+00 },
     { 7.703000e+02,   4.655000e+00 },
     { 7.713000e+02,   4.590000e+00 },
     { 7.722000e+02,   4.690000e+00 },
     { 7.731000e+02,   4.828000e+00 },
     { 7.740000e+02,   4.799000e+00 },
     { 7.749000e+02,   4.524000e+00 },
     { 7.758000e+02,   4.125000e+00 },
     { 7.767000e+02,   3.767000e+00 },
     { 7.777000e+02,   3.509000e+00 },
     { 7.786000e+02,   3.321000e+00 },
     { 7.795000e+02,   3.163000e+00 },
     { 7.804000e+02,   3.027000e+00 },
     { 7.813000e+02,   2.910000e+00 },
     { 7.822000e+02,   2.794000e+00 },
     { 7.831000e+02,   2.672000e+00 },
     { 7.841000e+02,   2.555000e+00 },
     { 7.850000e+02,   2.461000e+00 },
     { 7.859000e+02,   2.390000e+00 },
     { 7.868000e+02,   2.328000e+00 },
     { 7.877000e+02,   2.262000e+00 },
     { 7.886000e+02,   2.184000e+00 },
     { 7.896000e+02,   2.100000e+00 },
     { 7.905000e+02,   2.028000e+00 },
     { 7.914000e+02,   1.982000e+00 },
     { 7.923000e+02,   1.944000e+00 },
     { 7.932000e+02,   1.898000e+00 },
     { 7.941000e+02,   1.845000e+00 },
     { 7.950000e+02,   1.798000e+00 },
     { 7.960000e+02,   1.760000e+00 },
     { 7.969000e+02,   1.730000e+00 },
     { 7.978000e+02,   1.707000e+00 },
     { 7.987000e+02,   1.683000e+00 },
     { 7.996000e+02,   1.649000e+00 },
     { 8.005000e+02,   1.614000e+00 },
     { 8.014000e+02,   1.590000e+00 },
     { 8.024000e+02,   1.567000e+00 },
     { 8.033000e+02,   1.530000e+00 },
     { 8.042000e+02,   1.484000e+00 },
     { 8.051000e+02,   1.459000e+00 },
     { 8.060000e+02,   1.463000e+00 },
     { 8.069000e+02,   1.468000e+00 },
     { 8.079000e+02,   1.450000e+00 },
     { 8.088000e+02,   1.413000e+00 },
     { 8.097000e+02,   1.381000e+00 },
     { 8.106000e+02,   1.363000e+00 },
     { 8.115000e+02,   1.355000e+00 },
     { 8.124000e+02,   1.348000e+00 },
     { 8.133000e+02,   1.339000e+00 },
     { 8.143000e+02,   1.318000e+00 },
     { 8.152000e+02,   1.292000e+00 },
     { 8.161000e+02,   1.277000e+00 },
     { 8.170000e+02,   1.274000e+00 },
     { 8.179000e+02,   1.269000e+00 },
     { 8.188000e+02,   1.253000e+00 },
     { 8.197000e+02,   1.235000e+00 },
     { 8.207000e+02,   1.226000e+00 },
     { 8.216000e+02,   1.221000e+00 },
     { 8.225000e+02,   1.219000e+00 },
     { 8.234000e+02,   1.226000e+00 },
     { 8.243000e+02,   1.243000e+00 },
     { 8.252000e+02,   1.265000e+00 },
     { 8.261000e+02,   1.280000e+00 },
     { 8.271000e+02,   1.280000e+00 },
     { 8.280000e+02,   1.272000e+00 },
     { 8.289000e+02,   1.280000e+00 },
     { 8.298000e+02,   1.324000e+00 },
     { 8.307000e+02,   1.435000e+00 },
     { 8.316000e+02,   1.672000e+00 },
     { 8.326000e+02,   2.106000e+00 },
     { 8.335000e+02,   2.651000e+00 },
     { 8.344000e+02,   2.664000e+00 },
     { 8.353000e+02,   2.104000e+00 },
     { 8.362000e+02,   1.659000e+00 },
     { 8.371000e+02,   1.442000e+00 },
     { 8.380000e+02,   1.362000e+00 },
     { 8.390000e+02,   1.332000e+00 },
     { 8.399000e+02,   1.330000e+00 },
     { 8.408000e+02,   1.380000e+00 },
     { 8.417000e+02,   1.446000e+00 },
     { 8.426000e+02,   1.427000e+00 },
     { 8.435000e+02,   1.307000e+00 },
     { 8.444000e+02,   1.183000e+00 },
     { 8.454000e+02,   1.121000e+00 },
     { 8.463000e+02,   1.109000e+00 },
     { 8.472000e+02,   1.115000e+00 },
     { 8.481000e+02,   1.121000e+00 },
     { 8.490000e+02,   1.120000e+00 },
     { 8.499000e+02,   1.104000e+00 },
     { 8.509000e+02,   1.084000e+00 },
     { 8.518000e+02,   1.084000e+00 },
     { 8.527000e+02,   1.102000e+00 },
     { 8.536000e+02,   1.115000e+00 },
     { 8.545000e+02,   1.109000e+00 },
     { 8.554000e+02,   1.098000e+00 },
     { 8.563000e+02,   1.108000e+00 },
     { 8.573000e+02,   1.154000e+00 },
     { 8.582000e+02,   1.214000e+00 },
     { 8.591000e+02,   1.248000e+00 },
     { 8.600000e+02,   1.242000e+00 },
     { 8.609000e+02,   1.216000e+00 },
     { 8.618000e+02,   1.187000e+00 },
     { 8.627000e+02,   1.154000e+00 },
     { 8.637000e+02,   1.118000e+00 },
     { 8.646000e+02,   1.092000e+00 },
     { 8.655000e+02,   1.078000e+00 },
     { 8.664000e+02,   1.072000e+00 },
     { 8.673000e+02,   1.076000e+00 },
     { 8.682000e+02,   1.093000e+00 },
     { 8.691000e+02,   1.113000e+00 },
     { 8.701000e+02,   1.118000e+00 },
     { 8.710000e+02,   1.113000e+00 },
     { 8.719000e+02,   1.117000e+00 },
     { 8.728000e+02,   1.131000e+00 },
     { 8.737000e+02,   1.137000e+00 },
     { 8.746000e+02,   1.133000e+00 },
     { 8.756000e+02,   1.138000e+00 },
     { 8.765000e+02,   1.155000e+00 },
     { 8.774000e+02,   1.169000e+00 },
     { 8.783000e+02,   1.168000e+00 },
     { 8.792000e+02,   1.157000e+00 },
     { 8.801000e+02,   1.151000e+00 },
     { 8.810000e+02,   1.165000e+00 },
     { 8.820000e+02,   1.193000e+00 },
     { 8.829000e+02,   1.213000e+00 },
     { 8.838000e+02,   1.208000e+00 },
     { 8.847000e+02,   1.197000e+00 },
     { 8.856000e+02,   1.210000e+00 },
     { 8.865000e+02,   1.242000e+00 },
     { 8.874000e+02,   1.258000e+00 },
     { 8.884000e+02,   1.251000e+00 },
     { 8.893000e+02,   1.246000e+00 },
     { 8.902000e+02,   1.261000e+00 },
     { 8.911000e+02,   1.291000e+00 },
     { 8.920000e+02,   1.329000e+00 },
     { 8.929000e+02,   1.372000e+00 },
     { 8.939000e+02,   1.410000e+00 },
     { 8.948000e+02,   1.430000e+00 },
     { 8.957000e+02,   1.430000e+00 },
     { 8.966000e+02,   1.424000e+00 },
     { 8.975000e+02,   1.434000e+00 },
     { 8.984000e+02,   1.469000e+00 },
     { 8.993000e+02,   1.519000e+00 },
     { 9.003000e+02,   1.566000e+00 },
     { 9.012000e+02,   1.604000e+00 },
     { 9.021000e+02,   1.645000e+00 },
     { 9.030000e+02,   1.724000e+00 },
     { 9.039000e+02,   1.860000e+00 },
     { 9.048000e+02,   2.032000e+00 },
     { 9.057000e+02,   2.186000e+00 },
     { 9.067000e+02,   2.284000e+00 },
     { 9.076000e+02,   2.356000e+00 },
     { 9.085000e+02,   2.474000e+00 },
     { 9.094000e+02,   2.693000e+00 },
     { 9.103000e+02,   3.045000e+00 },
     { 9.112000e+02,   3.525000e+00 },
     { 9.121000e+02,   4.084000e+00 },
     { 9.131000e+02,   4.536000e+00 },
     { 9.140000e+02,   4.683000e+00 },
     { 9.149000e+02,   4.704000e+00 },
     { 9.158000e+02,   4.677000e+00 },
     { 9.167000e+02,   4.599000e+00 },
     { 9.176000e+02,   4.474000e+00 },
     { 9.186000e+02,   4.222000e+00 },
     { 9.195000e+02,   3.815000e+00 },
     { 9.204000e+02,   3.365000e+00 },
     { 9.213000e+02,   2.959000e+00 },
     { 9.222000e+02,   2.628000e+00 },
     { 9.231000e+02,   2.382000e+00 },
     { 9.240000e+02,   2.213000e+00 },
     { 9.250000e+02,   2.096000e+00 },
     { 9.259000e+02,   2.006000e+00 },
     { 9.268000e+02,   1.930000e+00 },
     { 9.277000e+02,   1.867000e+00 },
     { 9.286000e+02,   1.820000e+00 },
     { 9.295000e+02,   1.797000e+00 },
     { 9.304000e+02,   1.801000e+00 },
     { 9.314000e+02,   1.822000e+00 },
     { 9.323000e+02,   1.840000e+00 },
     { 9.332000e+02,   1.838000e+00 },
     { 9.341000e+02,   1.822000e+00 },
     { 9.350000e+02,   1.814000e+00 },
     { 9.359000e+02,   1.816000e+00 },
     { 9.369000e+02,   1.824000e+00 },
     { 9.378000e+02,   1.840000e+00 },
     { 9.387000e+02,   1.864000e+00 },
     { 9.396000e+02,   1.890000e+00 },
     { 9.405000e+02,   1.912000e+00 },
     { 9.414000e+02,   1.938000e+00 },
     { 9.423000e+02,   1.978000e+00 },
     { 9.433000e+02,   2.023000e+00 },
     { 9.442000e+02,   2.063000e+00 },
     { 9.451000e+02,   2.097000e+00 },
     { 9.460000e+02,   2.132000e+00 },
     { 9.469000e+02,   2.174000e+00 },
     { 9.478000e+02,   2.226000e+00 },
     { 9.487000e+02,   2.294000e+00 },
     { 9.497000e+02,   2.369000e+00 },
     { 9.506000e+02,   2.437000e+00 },
     { 9.515000e+02,   2.492000e+00 },
     { 9.524000e+02,   2.541000e+00 },
     { 9.533000e+02,   2.599000e+00 },
     { 9.542000e+02,   2.672000e+00 },
     { 9.551000e+02,   2.758000e+00 },
     { 9.561000e+02,   2.856000e+00 },
     { 9.570000e+02,   2.973000e+00 },
     { 9.579000e+02,   3.107000e+00 },
     { 9.588000e+02,   3.245000e+00 },
     { 9.597000e+02,   3.386000e+00 },
     { 9.606000e+02,   3.558000e+00 },
     { 9.616000e+02,   3.776000e+00 },
     { 9.625000e+02,   4.026000e+00 },
     { 9.634000e+02,   4.280000e+00 },
     { 9.643000e+02,   4.530000e+00 },
     { 9.652000e+02,   4.789000e+00 },
     { 9.661000e+02,   5.048000e+00 },
     { 9.670000e+02,   5.241000e+00 },
     { 9.680000e+02,   5.305000e+00 },
     { 9.689000e+02,   5.252000e+00 },
     { 9.698000e+02,   5.164000e+00 },
     { 9.707000e+02,   5.136000e+00 },
     { 9.716000e+02,   5.217000e+00 },
     { 9.725000e+02,   5.363000e+00 },
     { 9.734000e+02,   5.436000e+00 },
     { 9.744000e+02,   5.368000e+00 },
     { 9.753000e+02,   5.253000e+00 },
     { 9.762000e+02,   5.182000e+00 },
     { 9.771000e+02,   5.180000e+00 },
     { 9.780000e+02,   5.241000e+00 },
     { 9.789000e+02,   5.332000e+00 },
     { 9.799000e+02,   5.391000e+00 },
     { 9.808000e+02,   5.367000e+00 },
     { 9.817000e+02,   5.265000e+00 },
     { 9.826000e+02,   5.119000e+00 },
     { 9.835000e+02,   4.969000e+00 },
     { 9.844000e+02,   4.853000e+00 },
     { 9.853000e+02,   4.786000e+00 },
     { 9.863000e+02,   4.746000e+00 },
     { 9.872000e+02,   4.717000e+00 },
     { 9.881000e+02,   4.719000e+00 },
     { 9.890000e+02,   4.783000e+00 },
     { 9.899000e+02,   4.900000e+00 },
     { 9.908000e+02,   5.050000e+00 },
     { 9.917000e+02,   5.205000e+00 },
     { 9.927000e+02,   5.285000e+00 },
     { 9.936000e+02,   5.224000e+00 },
     { 9.945000e+02,   5.124000e+00 },
     { 9.954000e+02,   5.137000e+00 },
     { 9.963000e+02,   5.271000e+00 },
     { 9.972000e+02,   5.363000e+00 },
     { 9.981000e+02,   5.297000e+00 },
     { 9.991000e+02,   5.202000e+00 },
     { 1.000000e+03,   5.193000e+00 }
   };

   public double [][] getTau ( double minFreq, double maxFreq )
   {
      int start;
      int endj;
      int j;
      double[][] extract = null;

      minFreq = minFreq * 1.0E-9;
      maxFreq = maxFreq * 1.0E-9;
      start = 0;

      for ( j=0; j<atmosphere.length; j++ )
      {
         if ( atmosphere[j][0] > maxFreq ) break;
         if ( ( atmosphere[j][0] >= minFreq ) && ( start == 0 ) )
         {
            start = j;
         }
      }
      endj = j;

      if ( endj >= start )
      {
         extract = new double [endj-start+1][2];
         for ( j=start; j<=endj; j++ )
         {
            extract[j-start][0] = atmosphere[j][0] * 1.0E9;
            extract[j-start][1] = atmosphere[j][1];
         }
      }

      return extract;
   }
}
