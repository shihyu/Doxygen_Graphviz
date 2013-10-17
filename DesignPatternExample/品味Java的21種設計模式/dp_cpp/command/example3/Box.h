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
				namespace example3
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
					///	 <summary> * �ṩ���ͻ�ʹ�ã����ܲ���Ӧ�û������൱�ڿ�����ť�����´����ķ��� </summary>
					///	 
						virtual void openButtonPressed();
					///	
					///	 <summary> * ��������������� </summary>
					///	 
					private:
						Command *resetCommand;
					///	
					///	 <summary> * ������������������� </summary>
					///	 * <param name="command">  </param>
					///	 
					public:
						virtual void setResetCommand(Command *command);
					///	
					///	 <summary> * �ṩ���ͻ�ʹ�ã����ܲ���Ӧ�û������൱��������ť�����´����ķ��� </summary>
					///	 
						virtual void resetButtonPressed();
					};

				}
			}
		}
	}
}