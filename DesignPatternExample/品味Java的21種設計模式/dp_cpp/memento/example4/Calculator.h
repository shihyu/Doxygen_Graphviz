#pragma once

#include "Command.h"
#include "Memento.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace memento
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
					///	
					///	 <summary> * ���������Ӧ�ı���¼�������ʷ��¼���ڳ���ʱ����
					///	 * ���ڶ���ÿ��������󣬳�����������״̬�ǲ�һ���ģ�
					///	 * �����ǻص��������ǰ��״̬���������ǻص�����������״̬��
					///	 * ��˶�ÿһ�����ʹ��һ������¼�������������¼��Ӧ��״̬ </summary>
					///	 
						std::vector<Memento[]> undoMementos;
					///	
					///	 <summary> * �����������Ӧ�ı���¼�������ʷ��¼���ڻָ�ʱ����,
					///	 * ����������Ԫ�أ���һ��������ִ��ǰ��״̬���ڶ���������ִ�к��״̬ </summary>
					///	 
						std::vector<Memento[]> redoMementos;

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
								undoMementos = std::vector<Memento[]>();
								redoMementos = std::vector<Memento[]>();

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