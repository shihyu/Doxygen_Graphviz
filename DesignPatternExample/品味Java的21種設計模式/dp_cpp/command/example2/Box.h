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
				namespace example2
				{
					///
					/// <summary> * ������󣬱����а�ť�����а�ť��Ӧ��������� </summary>
					/// 
					class Box
					{
					///	
					///	 <summary> * ����������� </summary>
					///	 
					private:
						Command *openCommand;
					///	
					///	 <summary> * ���ÿ���������� </summary>
					///	 * <param name="command"> ����������� </param>
					///	 
					public:
						virtual void setOpenCommand(Command *command);
					///	
					///	 <summary> * �ṩ���ͻ�ʹ�ã����ܲ���Ӧ�û������൱�ڰ�ť�����´����ķ��� </summary>
					///	 
						virtual void openButtonPressed();
					};

				}
			}
		}
	}
}