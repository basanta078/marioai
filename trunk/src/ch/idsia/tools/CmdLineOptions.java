/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.tools;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 25, 2009
 * Time: 9:05:20 AM
 * Package: ch.idsia.tools
 */

/**
 * The <code>CmdLineOptions</code> class handles the command-line options
 * It sets up parameters from command line if there are any.
 * Defaults are used otherwise.
 *
 * @author Sergey Karakovskiy
 * @version 1.0, Apr 25, 2009
 * @see ch.idsia.utils.ParameterContainer
 * @see ch.idsia.benchmark.mario.simulation.SimulationOptions
 * @since MarioAI0.1
 */

public final class CmdLineOptions extends SimulationOptions
{
private static final HashMap<String, CmdLineOptions> CmdLineOptionsMapString = new HashMap<String, CmdLineOptions>();
private String optionsAsString = "";

final private Point marioInitialPos = new Point();

public CmdLineOptions(String[] args)
{
    super();
    this.setArgs(args);
}

//    @Deprecated

public CmdLineOptions(String args)
{
    //USE CmdLineOptions.getCmdLineOptionsClassByString(String args) method
    super();
    this.setArgs(args);
}

public CmdLineOptions()
{
    super();
    this.setArgs("");
}

public void setArgs(String argString)
{
    if (!"".equals(argString))
        this.setArgs(argString.trim().split("\\s+"));
    else
        this.setArgs((String[]) null);
}

public String asString()
{
    return optionsAsString;
}

public void setArgs(String[] args)
{
//        if (args.length > 0 && !args[0].startsWith("-") /*starts with a path to agent then*/)
//        {
//            this.setAgent(args[0]);
//
//            String[] shiftedargs = new String[args.length - 1];
//            System.arraycopy(args, 1, shiftedargs, 0, args.length - 1);
//            this.setUpOptions(shiftedargs);
//        }
//        else
    if (args != null)
        for (String s : args)
            optionsAsString += s + " ";

    this.setUpOptions(args);

    if (isEcho())
    {
        this.printOptions(false);
    }
    GlobalOptions.receptiveFieldWidth = getReceptiveFieldWidth();
    GlobalOptions.receptiveFieldHeight = getReceptiveFieldHeight();
    GlobalOptions.VISUAL_COMPONENT_HEIGHT = getViewHeight();
    GlobalOptions.VISUAL_COMPONENT_WIDTH = getViewWidth();
//        Environment.ObsWidth = GlobalOptions.receptiveFieldWidth/2;
//        Environment.ObsHeight = GlobalOptions.receptiveFieldHeight/2;
    GlobalOptions.isShowReceptiveField = isReceptiveFieldVisualized();
    GlobalOptions.isGameplayStopped = isStopGamePlay();
}

public float getMarioGravity()
{
    // TODO: getMarioGravity, doublecheck if unit test is present and remove if fixed
    return f(getParameterValue("-mgr"));
}

public float getCreaturesGravity()
{
    // TODO: getCreaturesGravity, same as for mgr
    return f(getParameterValue("-cgr"));
}

public int getViewWidth()
{
    return i(getParameterValue("-vw"));
}

public int getViewHeight()
{
    return i(getParameterValue("-vh"));
}

public void printOptions(boolean singleLine)
{
    System.out.println("\n[MarioAI] : Options have been set to:");
    for (Map.Entry<String, String> el : optionsHashMap.entrySet())
        if (singleLine)
            System.out.print(el.getKey() + " " + el.getValue() + " ");
        else
            System.out.println(el.getKey() + " " + el.getValue() + " ");
}

public static CmdLineOptions getOptionsByString(String argString)
{
    // TODO: verify validity of this method, add unit tests
    if (CmdLineOptionsMapString.get(argString) == null)
    {
        final CmdLineOptions value = new CmdLineOptions(argString.trim().split("\\s+"));
        CmdLineOptionsMapString.put(argString, value);
        return value;
    }
    return CmdLineOptionsMapString.get(argString);
}

public static CmdLineOptions getDefaultOptions()
{
    return getOptionsByString("");
}

public boolean isToolsConfigurator()
{
    return b(getParameterValue("-tc"));
}

public boolean isGameViewer()
{
    return b(getParameterValue("-gv"));
}

public boolean isGameViewerContinuousUpdates()
{
    return b(getParameterValue("-gvc"));
}

public boolean isEcho()
{
    return b(getParameterValue("-echo"));
}

public boolean isStopGamePlay()
{
    return b(getParameterValue("-stop"));
}

public String getPyAmiCoModuleName()
{
    return getParameterValue("-pym");
}

public int getReceptiveFieldWidth()
{
    int ret = i(getParameterValue("-rfw"));

    if (ret % 2 == 0)
    {
        System.err.println("\n[MarioAI WARNING] : Wrong value for receptive field width: " + ret++ +
                " ; receptive field width set to " + ret);
        setParameterValue("-rfw", s(ret));
    }
    return ret;
}

public int getReceptiveFieldHeight()
{
    int ret = i(getParameterValue("-rfh"));
    if (ret % 2 == 0)
    {
        System.err.println("\n[MarioAI WARNING] : Wrong value for receptive field height: " + ret++ +
                " ; receptive field height set to " + ret);
        setParameterValue("-rfh", s(ret));
    }
    return ret;
}

public boolean isReceptiveFieldVisualized()
{
    return b(getParameterValue("-srf"));
}

public Point getMarioInitialPos()
{
    marioInitialPos.x = i(getParameterValue("-mix"));
    marioInitialPos.y = i(getParameterValue("-miy"));
    return marioInitialPos;
}

public void reset()
{
    optionsHashMap.clear();
}
}