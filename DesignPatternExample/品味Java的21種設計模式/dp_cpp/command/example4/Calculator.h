#pragma once

#include "Command.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example4
				{
					///
					/// <summary> * �������࣬���������мӷ���ť��������ť�����г����ͻָ��İ�ť </summary>
					/// 
					class Calculator
					{
					///	
					///	 <summary> * ����Ĳ�������ʷ��¼���ڳ���ʱ���� </summary>
					///	 
					private:
						std::vector<Command*> undoCmds;
					///	
					///	 <summary> * �����������ʷ��¼���ڻָ�ʱ���� </summary>
					///	 
						std::vector<Command*> redoCmds;

						Command *addCmd;
						Command *substractCmd;
					public:
						virtual void setAddCmd(Command *addCmd);
						virtual void setSubstractCmd(Command *substractCmd);
						virtual void addPressed();
						virtual void substractPressed();
						virtual void undoPressed();
						virtual void redoPressed();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								undoCmds = std::vector<Command*>();
								redoCmds = std::vector<Command*>();

								initialized = true;
							}
						}

public:
	Calculator()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}