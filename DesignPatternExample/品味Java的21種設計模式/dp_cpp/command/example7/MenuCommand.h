#pragma once

#include "Command.h"
#include "CookApi.h"
#include "CommandQueue.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example7
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
						virtual void setCookApi(CookApi *cookApi);
						virtual int getTableNum();
					///	
					///	 <summary> * ��ȡ�˵��еĶ��������� </summary>
					///	 * <returns> �˵��еĶ��������� </returns>
					///	 
						virtual Collection<Command*> *getCommands();

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