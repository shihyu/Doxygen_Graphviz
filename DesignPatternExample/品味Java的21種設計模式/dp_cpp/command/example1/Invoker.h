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
				namespace example1
				{
					///
					/// <summary> * ������ </summary>
					/// 
					class Invoker
					{
					///	
					///	 <summary> * ����������� </summary>
					///	 
					private:
						Command *command;
					///	
					///	 <summary> * ���õ����߳��е�������� </summary>
					///	 * <param name="command"> ������� </param>
					///	 
					public:
						virtual void setCommand(Command *command);
					///	
					///	 <summary> * ʾ�ⷽ����Ҫ������ִ������ </summary>
					///	 
						virtual void runCommand();
					};

				}
			}
		}
	}
}