#pragma once

#include "MenuCommand.h"
#include "Command.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example6
				{
					///
					/// <summary> * ����Ա��������ϲ˵�,������ִ�е��� </summary>
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