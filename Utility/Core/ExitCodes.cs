namespace Utility.Core
{
    public enum ExitCodes
    {
        #region OK
        GenericOK = 0,
        UserExitRequest = 1,
        #endregion

        #region Bad
        UnknownError = -1,
        #endregion
    }
}
