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
				namespace example5
				{
					///
					/// <summary> * �˵������Ǹ���������� </summary>
					/// 
					class MenuCommand : public Command
					{
					///	
					///	 <summary> * ������¼��ϱ��˵��Ķ����Ʒ��Ҳ���Ƕ��������� </summary>
					///	 
					private:
						Collection<Command*> *col;
					///	
					///	 <summary> * ��ˣ��Ѳ�Ʒ���뵽�˵��� </summary>
					///	 * <param name="cmd"> �ͻ���Ĳ� </param>
					///	 
					public:
						virtual void addCommand(Command *cmd);

						virtual void execute();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								col = std::vector<Command*>();

								initialized = true;
							}
						}

public:
	MenuCommand()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}