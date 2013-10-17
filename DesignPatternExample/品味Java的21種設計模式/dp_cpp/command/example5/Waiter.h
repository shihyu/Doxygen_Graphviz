#pragma once

#include "MenuCommand.h"
#include "Command.h"
#include "CookApi.h"
#include "HotCook.h"
#include "CoolCook.h"
#include "DuckCommand.h"
#include "ChopCommand.h"
#include "PorkCommand.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example5
				{
					///
					/// <summary> * ����Ա��������ϲ˵���������װÿ���˺;����ʵ���ߣ�
					/// * ������ִ�е��ã��൱�ڱ�׼Commandģʽ��Client+Invoker </summary>
					/// 
					class Waiter
					{
					///	
					///	 <summary> * ����һ����������󡪡��˵� </summary>
					///	 
					private:
						MenuCommand *menuCommand;
					///	
					///	 <summary> * �ͻ���� </summary>
					///	 * <param name="cmd"> �ͻ���Ĳˣ�ÿ������һ��������� </param>
					///	 
					public:
						virtual void orderDish(Command *cmd);
					///	
					///	 <summary> * �ͻ������ϣ���ʾҪִ�������ˣ��������ִ�в˵����������� </summary>
					///	 
						virtual void orderOver();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								menuCommand = new MenuCommand();

								initialized = true;
							}
						}

public:
	Waiter()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}