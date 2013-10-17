#pragma once

#include "Command.h"
#include "Receiver.h"
#include <string>

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
					/// <summary> * ���������ʵ�ֶ��� </summary>
					/// 
					class ConcreteCommand : public Command
					{
					///	
					///	 <summary> * ������Ӧ�Ľ����߶��� </summary>
					///	 
					private:
						Receiver *receiver;
					///	
					///	 <summary> * ʾ�⣬�������������Լ���״̬ </summary>
					///	 
						std::string state;
					///	
					///	 <summary> * ���췽����������Ӧ�Ľ����߶��� </summary>
					///	 * <param name="receiver"> ��Ӧ�Ľ����߶��� </param>
					///	 
					public:
						ConcreteCommand(Receiver *receiver);

						virtual void execute();

					};

				}
			}
		}
	}
}