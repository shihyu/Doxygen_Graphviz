#pragma once

#include "Command.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example8
				{

					class Invoker
					{
					///	
					///	 <summary> * ����������� </summary>
					///	 
					private:
						Command *cmd;
					///	
					///	 <summary> * ����������� </summary>
					///	 * <param name="cmd"> ������� </param>
					///	 
					public:
						virtual void setCmd(Command *cmd);
					///	
					///	 <summary> * ��ʼ��ӡ </summary>
					///	 
						virtual void startPrint();
					};
				}
			}
		}
	}
}